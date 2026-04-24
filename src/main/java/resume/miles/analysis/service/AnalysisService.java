package resume.miles.analysis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import resume.miles.analysis.dto.AnalysisRequestDto;
import resume.miles.analysis.dto.DurationRequestDto;
import resume.miles.analysis.entity.AnalysisEntity;
import resume.miles.analysis.repository.AnalysisRepository;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final InterviewLinkRepository interviewLinkRepository;
    private final AnalysisRepository analysisRepository;
    private static final String BASE_DIR = "uploads/analysis/";

    @Value("${urls.baseUrl:http://localhost:8085}")
    private String baseUrl;

    // ─── Brand Colours ────────────────────────────────────────────────────────
    private static final BaseColor COLOR_PURPLE_DARK   = new BaseColor(60,  0,  60);   // #3C003C header bg
    private static final BaseColor COLOR_PURPLE        = new BaseColor(128,  0, 128);  // #800080 primary
    private static final BaseColor COLOR_PURPLE_PANEL  = new BaseColor(40,  0,  40);   // score panel
    private static final BaseColor COLOR_PURPLE_PALE   = new BaseColor(248, 240, 255); // #F8F0FF section bg
    private static final BaseColor COLOR_PURPLE_MID    = new BaseColor(237, 224, 245); // #EDE0F5 border tint

    private static final BaseColor COLOR_SUCCESS       = new BaseColor(22,  163,  74); // #16A34A
    private static final BaseColor COLOR_SUCCESS_PALE  = new BaseColor(240, 253, 244); // #F0FDF4
    private static final BaseColor COLOR_WARNING       = new BaseColor(202, 138,   4); // #CA8A04
    private static final BaseColor COLOR_WARNING_PALE  = new BaseColor(255, 251, 235); // #FFFBEB
    private static final BaseColor COLOR_DANGER        = new BaseColor(220,  38,  38); // #DC2626
    private static final BaseColor COLOR_DANGER_PALE   = new BaseColor(255, 241, 242); // #FFF1F2

    private static final BaseColor COLOR_WHITE         = BaseColor.WHITE;
    private static final BaseColor COLOR_BG            = new BaseColor(250, 250, 250); // #FAFAFA
    private static final BaseColor COLOR_BORDER        = new BaseColor(229, 231, 235); // #E5E7EB
    private static final BaseColor COLOR_TEXT          = new BaseColor( 17,  24,  39); // #111827
    private static final BaseColor COLOR_TEXT_MUTED    = new BaseColor(107, 114, 128); // #6B7280
    private static final BaseColor COLOR_BAR_BG        = new BaseColor(229, 231, 235); // track

    // ─── Fonts ────────────────────────────────────────────────────────────────
    // Header area
    private static final Font F_REPORT_TAG  = new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, new BaseColor(200,160,200));
    private static final Font F_BRAND       = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   new BaseColor(232,121,249));
    private static final Font F_CAND_NAME   = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   BaseColor.WHITE);
    private static final Font F_META        = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(220,180,220));
    private static final Font F_SCORE_NUM   = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD,   BaseColor.WHITE);
    private static final Font F_SCORE_LBL   = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, new BaseColor(220,180,220));
    private static final Font F_SCORE_HDR   = new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, new BaseColor(180,140,180));

    // Candidate card
    private static final Font F_CARD_TITLE  = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   COLOR_TEXT_MUTED);
    private static final Font F_LABEL       = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   COLOR_TEXT_MUTED);
    private static final Font F_VALUE       = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT);
    private static final Font F_LINK        = new Font(Font.FontFamily.HELVETICA, 10, Font.UNDERLINE, COLOR_PURPLE);
    private static final Font F_BADGE_AMBER = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   new BaseColor(146, 64, 14));

    // Section & body
    private static final Font F_SECTION     = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_PURPLE);
    private static final Font F_SECTION_GRN = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_SUCCESS);
    private static final Font F_SECTION_AMB = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_WARNING);
    private static final Font F_SECTION_RED = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_DANGER);

    private static final Font F_SKILL_NAME  = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   COLOR_TEXT);
    private static final Font F_SKILL_LVL   = new Font(Font.FontFamily.HELVETICA,  9, Font.ITALIC, COLOR_TEXT_MUTED);
    private static final Font F_SKILL_PCT   = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   COLOR_PURPLE);
    private static final Font F_BODY        = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT_MUTED);

    private static final Font F_BULLET_HEAD = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   COLOR_TEXT);
    private static final Font F_Q_TEXT      = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   COLOR_PURPLE);
    private static final Font F_Q_BODY      = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT);
    private static final Font F_Q_LBL       = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   COLOR_TEXT_MUTED);
    private static final Font F_Q_MISS_LBL  = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   COLOR_DANGER);

    private static final Font F_CAT_SCORE   = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   COLOR_TEXT); // color set per cell
    private static final Font F_CAT_LABEL   = new Font(Font.FontFamily.HELVETICA,  8, Font.BOLD,   COLOR_TEXT_MUTED);

    private static final Font F_FOOTER_BRAND = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,  COLOR_PURPLE);
    private static final Font F_FOOTER_SUB   = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL,COLOR_TEXT_MUTED);

    // =========================================================================
    // PUBLIC API  (unchanged logic)
    // =========================================================================

    @Transactional
    public void saveAnalysis(AnalysisRequestDto request) {
        InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        AnalysisEntity existingAnalysis = analysisRepository
                .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
                .orElse(null);

        if (existingAnalysis != null) {
            existingAnalysis.setAnalysis(request.getAnalysis());
            existingAnalysis.setStatus(1);
            analysisRepository.save(existingAnalysis);
        } else {
            AnalysisEntity newAnalysis = AnalysisEntity.builder()
                    .interviewLinkId(interviewLink.getId())
                    .analysis(request.getAnalysis())
                    .status(1)
                    .build();
            analysisRepository.save(newAnalysis);
        }
    }

    @Transactional
    public void saveDuration(DurationRequestDto request) {
        InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        AnalysisEntity existingAnalysis = analysisRepository
                .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
                .orElse(null);

        if (existingAnalysis != null) {
            existingAnalysis.setDuration(request.getDuration());
            analysisRepository.save(existingAnalysis);
        } else {
            AnalysisEntity newAnalysis = AnalysisEntity.builder()
                    .interviewLinkId(interviewLink.getId())
                    .analysis("{}")
                    .duration(request.getDuration())
                    .status(1)
                    .build();
            analysisRepository.save(newAnalysis);
        }
    }

    /** Minimal overload */
    public String generateAnalysisPdf(Long interviewId, String analysisJson) {
        return generateAnalysisPdf(interviewId, analysisJson,
                null, null, null, null, null, null, null, null);
    }

    /** Full overload */
    public String generateAnalysisPdf(
            Long   interviewId,
            String analysisJson,
            String candidateName,
            String candidateEmail,
            String candidatePhone,
            String interviewLink,
            String interviewDate,
            String interviewDuration,
            String videoLink,
            String transcriptionLink
    ) {
        try {
            File directory = new File(BASE_DIR);
            if (!directory.exists()) directory.mkdirs();

            String fileName = "analysis_" + interviewId + ".pdf";
            File   file     = new File(BASE_DIR + fileName);

            if (file.exists()) return "/uploads/analysis/" + fileName;

            // ── Parse JSON ────────────────────────────────────────────────────
            ObjectMapper mapper = new ObjectMapper();
            String raw = analysisJson == null ? "" : analysisJson.trim();
            if (raw.isEmpty()) throw new RuntimeException("Analysis JSON is empty");

            JsonNode analysis;
            try {
                if (raw.startsWith("{")) {
                    JsonNode root = mapper.readTree(raw);
                    analysis = root.has("analysis") ? root.get("analysis") : root;
                } else if (raw.startsWith("\"analysis\"")) {
                    String fragment = raw;
                    long openCount  = fragment.chars().filter(c -> c == '{').count();
                    long closeCount = fragment.chars().filter(c -> c == '}').count();
                    if (closeCount > openCount)
                        fragment = fragment.substring(0, fragment.lastIndexOf('}'));
                    JsonNode root = mapper.readTree("{" + fragment + "}");
                    analysis = root.get("analysis");
                    if (analysis == null) throw new RuntimeException("'analysis' key not found");
                } else {
                    throw new RuntimeException("Invalid JSON format: "
                            + raw.substring(0, Math.min(50, raw.length())));
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Invalid analysis JSON structure", e);
            }

            // ── Build PDF ─────────────────────────────────────────────────────
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            String fmtVideo = formatLink(videoLink);
            String fmtTrans = formatLink(transcriptionLink);
            String score    = analysis.path("overall_score").asText("N/A");

            // 1. Header banner
            addHeaderBanner(doc, candidateName, candidateEmail, candidatePhone, score);
            addSpacer(doc);

            // 2. Candidate + Session info cards (2-column)
            addInfoCards(doc, candidateName, candidateEmail, candidatePhone,
                    interviewDate, interviewDuration, interviewLink, fmtVideo, fmtTrans);
            addSpacer(doc);

            // 3. Category scores
            JsonNode catScores = analysis.path("category_scores");
            if (!catScores.isMissingNode() && !catScores.isEmpty()) {
                addCategoryScores(doc, catScores);
                addSpacer(doc);
            }

            // 4. Must Have Skills
            addSectionHeader(doc, "Must Have Skills", COLOR_SUCCESS, F_SECTION_GRN);
            JsonNode mustSkills = analysis.path("must_to_have_skills");
            if (mustSkills.isArray())
                for (JsonNode skill : mustSkills) addSkillRow(doc, skill, COLOR_SUCCESS);
            addSpacer(doc);

            // 5. Good to Have Skills
            addSectionHeader(doc, "Good to Have Skills", COLOR_WARNING, F_SECTION_AMB);
            JsonNode goodSkills = analysis.path("good_to_have_skills");
            if (goodSkills.isArray())
                for (JsonNode skill : goodSkills) addSkillRow(doc, skill, COLOR_WARNING);
            addSpacer(doc);

            // 6. Top Strengths
            JsonNode strengths = analysis.path("top_strengths");
            if (strengths.isArray() && strengths.size() > 0) {
                addSectionHeader(doc, "Top Strengths", COLOR_SUCCESS, F_SECTION_GRN);
                for (JsonNode s : strengths) {
                    String heading = s.isTextual()
                            ? s.asText("")
                            : s.path("area").asText(s.path("strength").asText(""));
                    String desc = s.isTextual() ? "" : s.path("description").asText("");
                    addBulletItem(doc, heading, desc, COLOR_SUCCESS, COLOR_SUCCESS_PALE);
                }
                addSpacer(doc);
            }

            // 7. Areas for Improvement
            JsonNode improvements = analysis.path("areas_for_improvement");
            if (improvements.isArray() && improvements.size() > 0) {
                addSectionHeader(doc, "Areas for Improvement / Weakness", COLOR_DANGER, F_SECTION_RED);
                for (JsonNode area : improvements)
                    addBulletItem(doc,
                            area.path("area").asText(""),
                            area.path("description").asText(""),
                            COLOR_DANGER, COLOR_DANGER_PALE);
                addSpacer(doc);
            }

            // 8. Interview Q&A
            JsonNode questions = analysis.path("interview_questions_responses");
            if (questions.isArray() && questions.size() > 0) {
                addSectionHeader(doc, "Interview Questions & Responses", COLOR_PURPLE, F_SECTION);
                for (JsonNode q : questions) addQuestionBlock(doc, q);
                addSpacer(doc);
            }

            // 9. Overall Summary
            addSectionHeader(doc, "Overall Interview Summary", COLOR_PURPLE, F_SECTION);
            addSummaryBox(doc, analysis.path("overall_ai_summary").asText("N/A"));

            // 10. Footer
            addSpacer(doc);
            addFooter(doc);

            doc.close();
            return "/uploads/analysis/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    // =========================================================================
    // SECTION: HEADER BANNER
    // =========================================================================

    private void addHeaderBanner(Document doc,
                                  String name, String email, String phone,
                                  String score) throws Exception {

        PdfPTable banner = new PdfPTable(2);
        banner.setWidthPercentage(100);
        banner.setWidths(new float[]{3.2f, 1.0f});

        // ── Left cell: dark purple gradient effect using layered bg ───────────
        PdfPCell left = new PdfPCell();
        left.setBackgroundColor(COLOR_PURPLE_DARK);
        left.setBorder(Rectangle.NO_BORDER);
        left.setPaddingLeft(22);
        left.setPaddingTop(20);
        left.setPaddingBottom(20);
        left.setPaddingRight(14);
        left.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Report title tag line
        Paragraph tag = new Paragraph("INTERVIEW ANALYSIS REPORT", F_REPORT_TAG);
        tag.setSpacingAfter(4);
        left.addElement(tag);

        // Brand chip: "● InterviewFold"
        Paragraph brand = new Paragraph("\u25CF  InterviewFold", F_BRAND);
        brand.setSpacingAfter(10);
        left.addElement(brand);

        // Candidate name
        String displayName = (name != null && !name.isEmpty()) ? name : "Candidate";
        Paragraph namePara = new Paragraph(displayName, F_CAND_NAME);
        namePara.setSpacingAfter(6);
        left.addElement(namePara);

        // Email + phone chips
        if (email != null && !email.isEmpty()) {
            String contact = "\u2709  " + email;
            if (phone != null && !phone.isEmpty())
                contact += "     \u260E  " + phone;
            Paragraph metaPara = new Paragraph(contact, F_META);
            left.addElement(metaPara);
        }

        banner.addCell(left);

        // ── Right cell: darker panel, score ──────────────────────────────────
        PdfPCell right = new PdfPCell();
        right.setBackgroundColor(COLOR_PURPLE_PANEL);
        right.setBorder(Rectangle.NO_BORDER);
        right.setPadding(12);
        right.setHorizontalAlignment(Element.ALIGN_CENTER);
        right.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph scoreHdr = new Paragraph("SCORE", F_SCORE_HDR);
        scoreHdr.setAlignment(Element.ALIGN_CENTER);
        scoreHdr.setSpacingAfter(4);
        right.addElement(scoreHdr);

        Paragraph scoreNum = new Paragraph(score, F_SCORE_NUM);
        scoreNum.setAlignment(Element.ALIGN_CENTER);
        right.addElement(scoreNum);

        Paragraph scoreDenom = new Paragraph("/100", F_SCORE_LBL);
        scoreDenom.setAlignment(Element.ALIGN_CENTER);
        right.addElement(scoreDenom);

        Paragraph scoreLbl = new Paragraph("Overall", F_SCORE_LBL);
        scoreLbl.setAlignment(Element.ALIGN_CENTER);
        scoreLbl.setSpacingBefore(6);
        right.addElement(scoreLbl);

        banner.addCell(right);
        doc.add(banner);
    }

    // =========================================================================
    // SECTION: INFO CARDS  (2-column: Candidate Details | Session Info + Links)
    // =========================================================================

    private void addInfoCards(Document doc,
                               String name, String email, String phone,
                               String interviewDate, String interviewDuration,
                               String interviewLink, String videoLink,
                               String transcriptionLink) throws Exception {

        PdfPTable grid = new PdfPTable(2);
        grid.setWidthPercentage(100);
        grid.setWidths(new float[]{1f, 1f});
        grid.setSpacingBefore(4);

        // ── LEFT: Candidate Details ───────────────────────────────────────────
        PdfPCell leftCard = styledCard();
        leftCard.addElement(cardTitle("Candidate Details"));
        leftCard.addElement(infoRow("Full Name",   name));
        leftCard.addElement(infoRow("Email",       email));
        leftCard.addElement(infoRow("Phone",       phone));
        grid.addCell(leftCard);

        // ── RIGHT: Session Info ───────────────────────────────────────────────
        PdfPCell rightCard = styledCard();
        rightCard.addElement(cardTitle("Session Info"));
        rightCard.addElement(infoRow("Date",       interviewDate));
        rightCard.addElement(infoRow("Duration",   interviewDuration));
        // status badge
        Paragraph statusRow = new Paragraph();
        statusRow.setSpacingBefore(5);
        statusRow.add(new Chunk("Status:   ", F_LABEL));
        statusRow.add(new Chunk("  Pending Score  ", F_BADGE_AMBER));
        rightCard.addElement(statusRow);
        grid.addCell(rightCard);

        doc.add(grid);

        // ── Links card: full width ────────────────────────────────────────────
        PdfPTable linksWrapper = new PdfPTable(1);
        linksWrapper.setWidthPercentage(100);
        linksWrapper.setSpacingBefore(8);

        PdfPCell linksCard = styledCard();
        linksCard.addElement(cardTitle("Links & Resources"));
        linksCard.addElement(linkRow("Interview Link",     interviewLink));
        linksCard.addElement(linkRow("Video Recording",    videoLink));
        linksCard.addElement(linkRow("Transcript",         transcriptionLink));
        linksWrapper.addCell(linksCard);
        doc.add(linksWrapper);
    }

    /** Shared card cell style */
    private PdfPCell styledCard() {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_WHITE);
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(0.5f);
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(COLOR_PURPLE);
        cell.setPaddingTop(12);
        cell.setPaddingBottom(14);
        cell.setPaddingLeft(14);
        cell.setPaddingRight(14);
        return cell;
    }

    /** Card section title row */
    private Paragraph cardTitle(String title) {
        Paragraph p = new Paragraph(title.toUpperCase(), F_CARD_TITLE);
        p.setSpacingAfter(8);
        return p;
    }

    /** Plain label: value row */
    private Paragraph infoRow(String label, String value) {
        String display = (value != null && !value.isBlank()) ? value : "\u2014";
        Paragraph p = new Paragraph();
        p.setSpacingBefore(5);
        p.add(new Chunk(label + ":   ", F_LABEL));
        p.add(new Chunk(display, F_VALUE));
        return p;
    }

    /** Clickable link row */
    private Paragraph linkRow(String label, String url) {
        Paragraph p = new Paragraph();
        p.setSpacingBefore(5);
        p.add(new Chunk(label + ":   ", F_LABEL));
        if (url != null && !url.isBlank()) {
            Chunk link = new Chunk(url, F_LINK);
            link.setAction(new PdfAction(url));
            p.add(link);
        } else {
            p.add(new Chunk("\u2014", F_VALUE));
        }
        return p;
    }

    // =========================================================================
    // SECTION: CATEGORY SCORES
    // =========================================================================

    private void addCategoryScores(Document doc, JsonNode catScores) throws Exception {
        addSectionHeader(doc, "Category Scores", COLOR_PURPLE, F_SECTION);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(catScores).trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);

        List<String>  keys = new ArrayList<>();
        List<Integer> vals = new ArrayList<>();

        for (String pair : json.split(",")) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                keys.add(kv[0].trim().replace("\"", ""));
                try   { vals.add(Integer.parseInt(kv[1].trim().replace("\"", ""))); }
                catch (NumberFormatException e) { vals.add(0); }
            }
        }

        if (keys.isEmpty()) return;

        PdfPTable table = new PdfPTable(keys.size());
        table.setWidthPercentage(100);
        table.setSpacingBefore(4);

        for (int i = 0; i < keys.size(); i++) {
            int pct = vals.get(i);
            BaseColor scoreCol = scoreColor(pct);

            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(COLOR_PURPLE_PALE);
            cell.setBorderColor(COLOR_PURPLE_MID);
            cell.setBorderWidth(0.5f);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Font catFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, scoreCol);
            Paragraph scoreP = new Paragraph(String.valueOf(pct), catFont);
            scoreP.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(scoreP);

            cell.addElement(buildProgressBar(pct));

            Paragraph labelP = new Paragraph(toTitleCase(keys.get(i).replace("_", " ")), F_CAT_LABEL);
            labelP.setAlignment(Element.ALIGN_CENTER);
            labelP.setSpacingBefore(4);
            cell.addElement(labelP);

            table.addCell(cell);
        }

        doc.add(table);
    }

    // =========================================================================
    // SECTION: SECTION HEADER
    // =========================================================================

    private void addSectionHeader(Document doc, String text,
                                   BaseColor accentColor, Font font) throws Exception {
        PdfPTable hdr = new PdfPTable(1);
        hdr.setWidthPercentage(100);
        hdr.setSpacingBefore(14);
        hdr.setSpacingAfter(6);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(COLOR_PURPLE_PALE);
        cell.setBorderWidthLeft(4f);
        cell.setBorderColorLeft(accentColor);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColorBottom(COLOR_BORDER);
        cell.setPaddingTop(9);
        cell.setPaddingBottom(9);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(8);
        hdr.addCell(cell);

        doc.add(hdr);
    }

    // =========================================================================
    // SECTION: SKILL ROW
    // =========================================================================

    private void addSkillRow(Document doc, JsonNode skill, BaseColor accentColor) throws Exception {
        String skillName = skill.path("skill_name").asText("N/A");
        String desc      = skill.path("description").asText("");
        int    pct       = skill.path("percentage").asInt(0);
        String level     = getLevel(pct);

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_WHITE);
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(0.5f);
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(accentColor);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(12);

        // Top row: name | level | pct
        PdfPTable topRow = new PdfPTable(3);
        topRow.setWidths(new float[]{4f, 1.6f, 1.0f});
        topRow.setWidthPercentage(100);

        PdfPCell nameCell = new PdfPCell(new Phrase(skillName, F_SKILL_NAME));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(nameCell);

        PdfPCell lvlCell = new PdfPCell(new Phrase(level, F_SKILL_LVL));
        lvlCell.setBorder(Rectangle.NO_BORDER);
        lvlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        lvlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(lvlCell);

        PdfPCell pctCell = new PdfPCell(new Phrase(pct + "/100", F_SKILL_PCT));
        pctCell.setBorder(Rectangle.NO_BORDER);
        pctCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pctCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(pctCell);

        cell.addElement(topRow);
        cell.addElement(buildProgressBar(pct));

        if (!desc.isEmpty()) {
            Paragraph descPara = new Paragraph(desc, F_BODY);
            descPara.setSpacingBefore(5);
            descPara.setLeading(15f);
            cell.addElement(descPara);
        }

        card.addCell(cell);
        doc.add(card);
    }

    // =========================================================================
    // SECTION: PROGRESS BAR
    // =========================================================================

    private PdfPTable buildProgressBar(int pct) throws Exception {
        int filled = Math.max(0, Math.min(100, pct));
        int empty  = 100 - filled;

        if (filled == 0) {
            PdfPTable bar = new PdfPTable(1);
            bar.setWidthPercentage(100);
            bar.setSpacingBefore(4);
            PdfPCell c = new PdfPCell(new Phrase(" "));
            c.setFixedHeight(4f);
            c.setBackgroundColor(COLOR_BAR_BG);
            c.setBorder(Rectangle.NO_BORDER);
            bar.addCell(c);
            return bar;
        }
        if (empty == 0) {
            PdfPTable bar = new PdfPTable(1);
            bar.setWidthPercentage(100);
            bar.setSpacingBefore(4);
            PdfPCell c = new PdfPCell(new Phrase(" "));
            c.setFixedHeight(4f);
            c.setBackgroundColor(scoreColor(pct));
            c.setBorder(Rectangle.NO_BORDER);
            bar.addCell(c);
            return bar;
        }

        PdfPTable bar = new PdfPTable(2);
        bar.setWidths(new float[]{filled, empty});
        bar.setWidthPercentage(100);
        bar.setSpacingBefore(4);

        PdfPCell filledCell = new PdfPCell(new Phrase(" "));
        filledCell.setFixedHeight(4f);
        filledCell.setBackgroundColor(scoreColor(pct));
        filledCell.setBorder(Rectangle.NO_BORDER);
        bar.addCell(filledCell);

        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setFixedHeight(4f);
        emptyCell.setBackgroundColor(COLOR_BAR_BG);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        bar.addCell(emptyCell);

        return bar;
    }

    // =========================================================================
    // SECTION: BULLET ITEM (Strengths & Improvements)
    // =========================================================================

    private void addBulletItem(Document doc, String heading, String body,
                                BaseColor borderColor, BaseColor bgColor) throws Exception {
        if (heading == null || heading.isBlank()) return;

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(borderColor);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColorBottom(COLOR_BORDER);
        cell.setBackgroundColor(bgColor);
        cell.setPaddingTop(9);
        cell.setPaddingBottom(9);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(12);

        // heading with checkmark or warning icon
        Font iconFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,
                borderColor.equals(COLOR_SUCCESS) ? COLOR_SUCCESS : COLOR_DANGER);
        String icon = borderColor.equals(COLOR_SUCCESS) ? "\u2713  " : "\u26A0  ";

        Paragraph headPara = new Paragraph();
        headPara.add(new Chunk(icon, iconFont));
        headPara.add(new Chunk(heading, F_BULLET_HEAD));
        cell.addElement(headPara);

        if (body != null && !body.isBlank()) {
            Paragraph bodyPara = new Paragraph(body, F_BODY);
            bodyPara.setIndentationLeft(16);
            bodyPara.setSpacingBefore(3);
            bodyPara.setLeading(15f);
            cell.addElement(bodyPara);
        }

        card.addCell(cell);
        doc.add(card);
    }

    // =========================================================================
    // SECTION: INTERVIEW Q&A BLOCK
    // =========================================================================

    private void addQuestionBlock(Document doc, JsonNode q) throws Exception {
        String question = q.path("question").asText("");
        String response = q.path("response_summary").asText("");
        int    score    = q.path("score").asInt(0);

        BaseColor scoreCol = score >= 60 ? COLOR_SUCCESS
                           : score >= 30 ? COLOR_WARNING
                                         : COLOR_DANGER;
        BaseColor scoreBg  = score >= 60 ? COLOR_SUCCESS_PALE
                           : score >= 30 ? COLOR_WARNING_PALE
                                         : COLOR_DANGER_PALE;

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(6);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_WHITE);
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(0.5f);
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(COLOR_PURPLE);
        cell.setPaddingTop(11);
        cell.setPaddingBottom(11);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(12);

        // Q text + score badge on same line
        PdfPTable qRow = new PdfPTable(2);
        qRow.setWidths(new float[]{5f, 1.1f});
        qRow.setWidthPercentage(100);

        PdfPCell qCell = new PdfPCell(new Phrase("Q: " + question, F_Q_TEXT));
        qCell.setBorder(Rectangle.NO_BORDER);
        qRow.addCell(qCell);

        Font scoreBadgeFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, scoreCol);
        PdfPCell scoreCell = new PdfPCell(new Phrase("Score: " + score + "/100", scoreBadgeFont));
        scoreCell.setBorder(Rectangle.NO_BORDER);
        scoreCell.setBackgroundColor(scoreBg);
        scoreCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        scoreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        scoreCell.setPaddingLeft(6);
        scoreCell.setPaddingRight(6);
        scoreCell.setPaddingTop(3);
        scoreCell.setPaddingBottom(3);
        qRow.addCell(scoreCell);

        cell.addElement(qRow);
        cell.addElement(buildProgressBar(score));

        if (!response.isEmpty()) {
            Paragraph respLbl = new Paragraph("Response Summary", F_Q_LBL);
            respLbl.setSpacingBefore(9);
            respLbl.setSpacingAfter(3);
            cell.addElement(respLbl);
            Paragraph resp = new Paragraph(response, F_Q_BODY);
            resp.setLeading(15f);
            cell.addElement(resp);
        }

        JsonNode insights = q.path("key_insights");
        if (insights.isArray() && insights.size() > 0) {
            Paragraph insLbl = new Paragraph("Key Insights", F_Q_LBL);
            insLbl.setSpacingBefore(9);
            insLbl.setSpacingAfter(3);
            cell.addElement(insLbl);
            for (JsonNode insight : insights) {
                Paragraph ins = new Paragraph("\u203A  " + insight.asText(), F_BODY);
                ins.setSpacingBefore(2);
                cell.addElement(ins);
            }
        }

        JsonNode missed = q.path("missed_opportunities");
        if (missed.isArray() && missed.size() > 0) {
            Paragraph missLbl = new Paragraph("Missed Opportunities", F_Q_MISS_LBL);
            missLbl.setSpacingBefore(9);
            missLbl.setSpacingAfter(3);
            cell.addElement(missLbl);
            for (JsonNode m : missed) {
                Paragraph mp = new Paragraph("\u00D7  " + m.asText(), F_BODY);
                mp.setSpacingBefore(2);
                cell.addElement(mp);
            }
        }

        card.addCell(cell);
        doc.add(card);
    }

    // =========================================================================
    // SECTION: SUMMARY BOX
    // =========================================================================

    private void addSummaryBox(Document doc, String summaryText) throws Exception {
        PdfPTable wrapper = new PdfPTable(1);
        wrapper.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_PURPLE_PALE);
        cell.setBorderColor(COLOR_PURPLE_MID);
        cell.setBorderWidth(0.5f);
        cell.setBorderWidthLeft(4f);
        cell.setBorderColorLeft(COLOR_PURPLE);
        cell.setPaddingTop(12);
        cell.setPaddingBottom(12);
        cell.setPaddingLeft(14);
        cell.setPaddingRight(14);

        Paragraph p = new Paragraph(summaryText, F_Q_BODY);
        p.setLeading(17f);
        cell.addElement(p);
        wrapper.addCell(cell);
        doc.add(wrapper);
    }

    // =========================================================================
    // SECTION: FOOTER
    // =========================================================================

    private void addFooter(Document doc) throws Exception {
        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);
        footer.setSpacingBefore(10);

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthTop(0.5f);
        cell.setBorderColorTop(COLOR_BORDER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Chunk("\u2605  ", F_FOOTER_BRAND));
        p.add(new Chunk("InterviewFold", F_FOOTER_BRAND));
        p.add(new Chunk("  \u00B7  AI-powered hiring intelligence", F_FOOTER_SUB));
        cell.addElement(p);
        footer.addCell(cell);
        doc.add(footer);
    }

    // =========================================================================
    // UTILITIES
    // =========================================================================

    private String formatLink(String link) {
        if (link == null || link.isBlank()) return link;
        if (link.startsWith("http")) return link;
        String base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return base + (link.startsWith("/") ? "" : "/") + link;
    }

    private void addSpacer(Document doc) throws Exception {
        doc.add(Chunk.NEWLINE);
    }

    private BaseColor scoreColor(int pct) {
        if (pct >= 70) return COLOR_SUCCESS;
        if (pct >= 40) return COLOR_WARNING;
        return COLOR_DANGER;
    }

    private String getLevel(int pct) {
        if (pct >= 80) return "Advanced";
        if (pct >= 60) return "Intermediate";
        if (pct >= 30) return "Basic";
        return "Not Demonstrated";
    }

    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;
        StringBuilder sb = new StringBuilder();
        for (String word : input.split(" ")) {
            if (!word.isEmpty())
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
        }
        return sb.toString().trim();
    }
}





// package resume.miles.analysis.service;

// import java.io.File;
// import java.io.FileOutputStream;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.itextpdf.text.BaseColor;
// import com.itextpdf.text.Chunk;
// import com.itextpdf.text.Document;
// import com.itextpdf.text.Element;
// import com.itextpdf.text.Font;
// import com.itextpdf.text.PageSize;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.Phrase;
// import com.itextpdf.text.Rectangle;
// import com.itextpdf.text.pdf.PdfAction;
// import com.itextpdf.text.pdf.PdfPCell;
// import com.itextpdf.text.pdf.PdfPTable;
// import com.itextpdf.text.pdf.PdfWriter;

// import org.springframework.beans.factory.annotation.Value;
// import lombok.RequiredArgsConstructor;
// import resume.miles.analysis.dto.AnalysisRequestDto;
// import resume.miles.analysis.dto.DurationRequestDto;
// import resume.miles.analysis.entity.AnalysisEntity;
// import resume.miles.analysis.repository.AnalysisRepository;
// import resume.miles.interview.entity.InterviewLinkEntity;
// import resume.miles.interview.repository.InterviewLinkRepository;
// import tools.jackson.databind.JsonNode;
// import tools.jackson.databind.ObjectMapper;

// @Service
// @RequiredArgsConstructor
// public class AnalysisService {

//     private final InterviewLinkRepository interviewLinkRepository;
//     private final AnalysisRepository analysisRepository;
//     private static final String BASE_DIR = "uploads/analysis/";

//     @Value("${urls.baseUrl:http://localhost:8085}")
//     private String baseUrl;

//     // ─── Colours ─────────────────────────────────────────────────────────────
//     // Header / primary brand = #800080 purple (as requested)
//     private static final BaseColor COLOR_PRIMARY      = new BaseColor(128, 0, 128);    // #800080 purple
//     private static final BaseColor COLOR_PRIMARY_DARK = new BaseColor(96,  0,  96);    // darker purple for right panel
//     private static final BaseColor COLOR_SUCCESS      = new BaseColor(22, 163, 74);    // #16A34A green
//     private static final BaseColor COLOR_WARNING      = new BaseColor(202, 138, 4);    // #CA8A04 amber
//     private static final BaseColor COLOR_DANGER       = new BaseColor(220, 38,  38);   // #DC2626 red
//     private static final BaseColor COLOR_BG_LIGHT     = new BaseColor(252, 252, 255);  // near-white with a hint of purple
//     private static final BaseColor COLOR_BORDER       = new BaseColor(229, 231, 235);  // #E5E7EB neutral gray
//     private static final BaseColor COLOR_TEXT_DARK    = new BaseColor(17,  24,  39);   // #111827
//     private static final BaseColor COLOR_TEXT_MUTED   = new BaseColor(107, 114, 128);  // #6B7280
//     private static final BaseColor COLOR_SECTION_BG   = new BaseColor(250, 245, 255);  // very light purple tint
//     private static final BaseColor COLOR_BAR_BG       = new BaseColor(229, 231, 235);  // progress-bar track
//     private static final BaseColor COLOR_STRENGTH_BG  = new BaseColor(240, 253, 244);  // light green bg
//     private static final BaseColor COLOR_DANGER_BG    = new BaseColor(255, 241, 242);  // light red bg

//     // ─── Fonts ───────────────────────────────────────────────────────────────
//     private static final Font FONT_TITLE      = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_SUBTITLE   = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(220, 180, 220));
//     private static final Font FONT_NAME       = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_LABEL      = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_TEXT_MUTED);
//     private static final Font FONT_VALUE      = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, COLOR_TEXT_DARK);
//     private static final Font FONT_SKILL_NAME = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_TEXT_DARK);
//     private static final Font FONT_SKILL_LVL  = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, COLOR_TEXT_MUTED);
//     private static final Font FONT_SKILL_PCT  = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_PRIMARY);
//     private static final Font FONT_BODY       = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, COLOR_TEXT_MUTED);
//     private static final Font FONT_BULLET     = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_TEXT_DARK);
//     private static final Font FONT_SCORE_NUM  = new Font(Font.FontFamily.HELVETICA, 32, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_SCORE_LBL  = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(220, 180, 220));
//     private static final Font FONT_Q          = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_PRIMARY);
//     private static final Font FONT_Q_BODY     = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, COLOR_TEXT_DARK);
//     private static final Font FONT_LINK       = new Font(Font.FontFamily.HELVETICA, 11, Font.UNDERLINE, COLOR_PRIMARY);

//     // =========================================================================
//     // PUBLIC API
//     // =========================================================================

 

// @Transactional
// public void saveAnalysis(AnalysisRequestDto request) {
//     // 1. Validate Token
//     InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
//             .orElseThrow(() -> new RuntimeException("Invalid token"));

//     // 2. Find existing record (if saveDuration or a previous saveAnalysis ran first)
//     AnalysisEntity existingAnalysis = analysisRepository
//             .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
//             .orElse(null);

//     if (existingAnalysis != null) {
//         // UPDATE: Just overwrite the analysis text
//         existingAnalysis.setAnalysis(request.getAnalysis());
//         existingAnalysis.setStatus(1);
//         analysisRepository.save(existingAnalysis);
//     } else {
//         // INSERT: Create a brand new record
//         AnalysisEntity newAnalysis = AnalysisEntity.builder()
//                 .interviewLinkId(interviewLink.getId())
//                 .analysis(request.getAnalysis())
//                 .status(1)
//                 .build();
//         analysisRepository.save(newAnalysis);
//     }
// }

// @Transactional
// public void saveDuration(DurationRequestDto request) {
//     // 1. Validate Token
//     InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
//             .orElseThrow(() -> new RuntimeException("Invalid token"));

//     // 2. Find existing record (if saveAnalysis ran first)
//     AnalysisEntity existingAnalysis = analysisRepository
//             .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
//             .orElse(null);

//     if (existingAnalysis != null) {
//         // UPDATE: Just add the duration
//         existingAnalysis.setDuration(request.getDuration());
//         analysisRepository.save(existingAnalysis);
//     } else {
//         // INSERT: Create a stub record with empty JSON to satisfy the non-null constraint
//         AnalysisEntity newAnalysis = AnalysisEntity.builder()
//                 .interviewLinkId(interviewLink.getId())
//                 .analysis("{}") // Empty JSON placeholder
//                 .duration(request.getDuration())
//                 .status(1)
//                 .build();
//         analysisRepository.save(newAnalysis);
//     }
// }

 

//     /** Minimal overload — no candidate meta. */
//     public String generateAnalysisPdf(Long interviewId, String analysisJson) {
//         return generateAnalysisPdf(interviewId, analysisJson,
//                 null, null, null, null, null, null, null, null);
//     }

//     /**
//      * Full overload — call from InterviewService:
//      *
//      *   analysisService.generateAnalysisPdf(
//      *       link.getId(),
//      *       analysis.get().getAnalysis(),
//      *       interview.getCandidateName(),
//      *       interview.getEmail(),
//      *       interview.getPhoneNumber(),
//      *       link.getInterviewLink(),
//      *       interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null,
//      *       interview.getInterviewDuration(),
//      *       interview.getVideoLink(),
//      *       interview.getTranscriptionLink()
//      *   );
//      */
//     public String generateAnalysisPdf(
//             Long   interviewId,
//             String analysisJson,
//             String candidateName,
//             String candidateEmail,
//             String candidatePhone,
//             String interviewLink,
//             String interviewDate,
//             String interviewDuration,
//             String videoLink,
//             String transcriptionLink
//     ) {
//         try {
//             File directory = new File(BASE_DIR);
//             if (!directory.exists()) directory.mkdirs();

//             String fileName = "analysis_" + interviewId + ".pdf";
//             File   file     = new File(BASE_DIR + fileName);

//             if (file.exists()) {
//                 return "/uploads/analysis/" + fileName;
//             }

//             // ── Parse JSON ───────────────────────────────────────────────────
//             ObjectMapper mapper = new ObjectMapper();
//             String raw = analysisJson == null ? "" : analysisJson.trim();
//             if (raw.isEmpty()) throw new RuntimeException("Analysis JSON is empty");

//             JsonNode analysis;
//             try {
//                 if (raw.startsWith("{")) {
//                     JsonNode root = mapper.readTree(raw);
//                     analysis = root.has("analysis") ? root.get("analysis") : root;
//                 } else if (raw.startsWith("\"analysis\"")) {
//                     String fragment = raw;
//                     long openCount  = fragment.chars().filter(c -> c == '{').count();
//                     long closeCount = fragment.chars().filter(c -> c == '}').count();
//                     if (closeCount > openCount) {
//                         fragment = fragment.substring(0, fragment.lastIndexOf('}'));
//                     }
//                     JsonNode root = mapper.readTree("{" + fragment + "}");
//                     analysis = root.get("analysis");
//                     if (analysis == null) throw new RuntimeException("'analysis' key not found");
//                 } else {
//                     throw new RuntimeException("Invalid JSON format: "
//                             + raw.substring(0, Math.min(50, raw.length())));
//                 }
//             } catch (RuntimeException e) {
//                 throw e;
//             } catch (Exception e) {
//                 throw new RuntimeException("Invalid analysis JSON structure", e);
//             }

//             // ── Build PDF ────────────────────────────────────────────────────
//             Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
//             PdfWriter.getInstance(doc, new FileOutputStream(file));
//             doc.open();

//             // 1. Header banner
//             addHeaderBanner(doc, candidateName, candidateEmail, candidatePhone,
//                     analysis.path("overall_score").asText("N/A"));

//             addSpacer(doc);

//             String formattedVideoLink = (videoLink != null && !videoLink.isBlank() && !videoLink.startsWith("http")) 
//                     ? (baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl) + (videoLink.startsWith("/") ? "" : "/") + videoLink 
//                     : videoLink;
//             String formattedTranscriptionLink = (transcriptionLink != null && !transcriptionLink.isBlank() && !transcriptionLink.startsWith("http")) 
//                     ? (baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl) + (transcriptionLink.startsWith("/") ? "" : "/") + transcriptionLink 
//                     : transcriptionLink;

//             // 2. Candidate info card (vertical list)
//             addCandidateInfoCard(doc, candidateName, candidateEmail, candidatePhone,
//                     interviewDate, interviewDuration, interviewLink, formattedVideoLink, formattedTranscriptionLink);

//             addSpacer(doc);

//             // 3. Category scores
//             JsonNode catScores = analysis.path("category_scores");
//             if (!catScores.isMissingNode() && !catScores.isEmpty()) {
//                 addCategoryScores(doc, catScores);
//                 addSpacer(doc);
//             }

//             // 4. Must Have Skills
//             addSectionHeader(doc, "  Must Have Skills", COLOR_SUCCESS);
//             JsonNode mustSkills = analysis.path("must_to_have_skills");
//             if (mustSkills.isArray()) {
//                 for (JsonNode skill : mustSkills) addSkillRow(doc, skill, COLOR_SUCCESS);
//             }
//             addSpacer(doc);

//             // 5. Good to Have Skills
//             addSectionHeader(doc, "  Good to Have Skills", COLOR_WARNING);
//             JsonNode goodSkills = analysis.path("good_to_have_skills");
//             if (goodSkills.isArray()) {
//                 for (JsonNode skill : goodSkills) addSkillRow(doc, skill, COLOR_WARNING);
//             }
//             addSpacer(doc);

//             // 6. Strengths (bullet list with description — same style as improvements)
//             JsonNode strengths = analysis.path("top_strengths");
//             if (strengths.isArray() && strengths.size() > 0) {
//                 addSectionHeader(doc, "  Top Strengths", COLOR_SUCCESS);
//                 for (JsonNode s : strengths) {
//                     // top_strengths may be plain strings or objects {area, description}
//                     String heading = s.isTextual()
//                             ? s.asText("")
//                             : s.path("area").asText(s.path("strength").asText(""));
//                     String desc = s.isTextual() ? "" : s.path("description").asText("");
//                     addBulletItem(doc, heading, desc, COLOR_SUCCESS, COLOR_STRENGTH_BG);
//                 }
//                 addSpacer(doc);
//             }

//             // 7. Weaknesses / Areas for Improvement
//             JsonNode improvements = analysis.path("areas_for_improvement");
//             if (improvements.isArray() && improvements.size() > 0) {
//                 addSectionHeader(doc, "  Areas for Improvement/Weakness", COLOR_DANGER);
//                 for (JsonNode area : improvements) {
//                     addBulletItem(doc,
//                             area.path("area").asText(""),
//                             area.path("description").asText(""),
//                             COLOR_DANGER, COLOR_DANGER_BG);
//                 }
//                 addSpacer(doc);
//             }

//             // 8. Interview Q&A
//             JsonNode questions = analysis.path("interview_questions_responses");
//             if (questions.isArray() && questions.size() > 0) {
//                 addSectionHeader(doc, "  Interview Questions & Responses", COLOR_PRIMARY);
//                 for (JsonNode q : questions) addQuestionBlock(doc, q);
//                 addSpacer(doc);
//             }

//             // 9. Overall Summary
//             addSectionHeader(doc, "  Overall Interview Summary", COLOR_PRIMARY);
//             PdfPTable summaryBox = new PdfPTable(1);
//             summaryBox.setWidthPercentage(100);
//             PdfPCell summaryCell = new PdfPCell();
//             summaryCell.setBackgroundColor(COLOR_SECTION_BG);
//             summaryCell.setBorderColor(COLOR_PRIMARY);
//             summaryCell.setBorderWidth(0.5f);
//             summaryCell.setBorderWidthLeft(4f);
//             summaryCell.setPaddingTop(10);
//             summaryCell.setPaddingBottom(10);
//             summaryCell.setPaddingLeft(14);
//             summaryCell.setPaddingRight(14);
//             Paragraph summaryPara = new Paragraph(
//                     analysis.path("overall_ai_summary").asText("N/A"), FONT_Q_BODY);
//             summaryPara.setLeading(18f);
//             summaryCell.addElement(summaryPara);
//             summaryBox.addCell(summaryCell);
//             doc.add(summaryBox);

//             doc.close();
//             return "/uploads/analysis/" + fileName;

//         } catch (Exception e) {
//             e.printStackTrace();
//             throw new RuntimeException("PDF generation failed", e);
//         }
//     }

//     // =========================================================================
//     // SECTION: HEADER BANNER
//     // =========================================================================

//     private void addHeaderBanner(Document doc,
//                                   String name, String email, String phone,
//                                   String score) throws Exception {
//         PdfPTable banner = new PdfPTable(2);
//         banner.setWidthPercentage(100);
//         banner.setWidths(new float[]{3.0f, 1.1f});

//         // Left cell — purple brand
//         PdfPCell left = new PdfPCell();
//         left.setBackgroundColor(COLOR_PRIMARY);  // #800080
//         left.setBorder(Rectangle.NO_BORDER);
//         left.setPaddingLeft(20);
//         left.setPaddingTop(18);
//         left.setPaddingBottom(18);
//         left.setPaddingRight(12);
//         left.setVerticalAlignment(Element.ALIGN_MIDDLE);

//         Paragraph titlePara = new Paragraph("INTERVIEW ANALYSIS REPORT", FONT_TITLE);
//         left.addElement(titlePara);

//         String displayName = (name != null && !name.isEmpty()) ? name : "Candidate";
//         Paragraph namePara = new Paragraph(displayName, FONT_NAME);
//         namePara.setSpacingBefore(6);
//         left.addElement(namePara);

//         if (email != null && !email.isEmpty()) {
//             String contact = email + (phone != null && !phone.isEmpty() ? "   |   " + phone : "");
//             Paragraph contactPara = new Paragraph(contact, FONT_SUBTITLE);
//             contactPara.setSpacingBefore(3);
//             left.addElement(contactPara);
//         }
//         banner.addCell(left);

//         // Right cell — darker purple, score
//         PdfPCell right = new PdfPCell();
//         right.setBackgroundColor(COLOR_PRIMARY_DARK);
//         right.setBorder(Rectangle.NO_BORDER);
//         right.setPadding(10);
//         right.setHorizontalAlignment(Element.ALIGN_CENTER);
//         right.setVerticalAlignment(Element.ALIGN_MIDDLE);

//         Paragraph scoreNum = new Paragraph(score + "/100", FONT_SCORE_NUM);
//         scoreNum.setAlignment(Element.ALIGN_CENTER);
//         right.addElement(scoreNum);

//         Paragraph scoreLbl = new Paragraph("Overall Score", FONT_SCORE_LBL);
//         scoreLbl.setAlignment(Element.ALIGN_CENTER);
//         scoreLbl.setSpacingBefore(2);
//         right.addElement(scoreLbl);

//         banner.addCell(right);
//         doc.add(banner);
//     }

//     // =========================================================================
//     // SECTION: CANDIDATE INFO CARD  (vertical label: value list)
//     // =========================================================================

//     /**
//      * Renders candidate details as a clean vertical list — one field per line.
//      * Links (Interview, Video, Transcription) are clickable blue underlined text.
//      *
//      * PARAMETER ORDER (must match the call in generateAnalysisPdf):
//      *   name, email, phone, date, duration, interviewLink, videoLink, transcriptionLink
//      */
//     private void addCandidateInfoCard(Document doc,
//                                        String name,
//                                        String email,
//                                        String phone,
//                                        String interviewDate,
//                                        String interviewDuration,
//                                        String interviewLink,
//                                        String videoLink,
//                                        String transcriptionLink) throws Exception {

//         PdfPTable wrapper = new PdfPTable(1);
//         wrapper.setWidthPercentage(100);
//         wrapper.setSpacingBefore(4);

//         PdfPCell card = new PdfPCell();
//         card.setBackgroundColor(COLOR_BG_LIGHT);
//         card.setBorderColor(COLOR_BORDER);
//         card.setBorderWidth(0.5f);
//         card.setBorderWidthLeft(4f);
//         card.setBorderColorLeft(COLOR_PRIMARY);
//         card.setPaddingTop(12);
//         card.setPaddingBottom(14);
//         card.setPaddingLeft(16);
//         card.setPaddingRight(16);

//         // Plain text rows
//         card.addElement(makeLabelValueRow("Candidate Name",    name));
//         card.addElement(makeLabelValueRow("Email ID",          email));
//         card.addElement(makeLabelValueRow("Phone",             phone));
//         card.addElement(makeLabelValueRow("Interview Date",    interviewDate));
//         card.addElement(makeLabelValueRow("Interview Duration",interviewDuration));

//         // Link rows  ← order is important, must match parameter order above
//         card.addElement(makeLinkRow("Interview Link",    interviewLink));
//         card.addElement(makeLinkRow("Video Link",        videoLink));
//         card.addElement(makeLinkRow("Transcription Link",transcriptionLink));

//         wrapper.addCell(card);
//         doc.add(wrapper);
//     }

//     /** Plain text:  LABEL:   value */
//     private Paragraph makeLabelValueRow(String label, String value) {
//         String display = (value != null && !value.isBlank()) ? value : "\u2014";
//         Paragraph p = new Paragraph();
//         p.setSpacingBefore(5);
//         p.add(new Chunk(label + ":   ", FONT_LABEL));
//         p.add(new Chunk(display, FONT_VALUE));
//         return p;
//     }

//     /** Hyperlink row:  LABEL:   <clickable url> */
//     private Paragraph makeLinkRow(String label, String url) {
//         Paragraph p = new Paragraph();
//         p.setSpacingBefore(5);
//         p.add(new Chunk(label + ":   ", FONT_LABEL));

//         if (url != null && !url.isBlank()) {
//             Chunk link = new Chunk(url, FONT_LINK);
//             link.setAction(new PdfAction(url));   // real clickable hyperlink
//             p.add(link);
//         } else {
//             p.add(new Chunk("\u2014", FONT_VALUE));
//         }
//         return p;
//     }

//     // =========================================================================
//     // SECTION: CATEGORY SCORES
//     // =========================================================================

//     private void addCategoryScores(Document doc, JsonNode catScores) throws Exception {
//         addSectionHeader(doc, "  Category Scores", COLOR_PRIMARY);

//         ObjectMapper mapper = new ObjectMapper();
//         String json = mapper.writeValueAsString(catScores).trim();
//         if (json.startsWith("{")) json = json.substring(1);
//         if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);

//         List<String>  fieldNames = new ArrayList<>();
//         List<Integer> pctValues  = new ArrayList<>();

//         for (String pair : json.split(",")) {
//             String[] kv = pair.split(":");
//             if (kv.length == 2) {
//                 String key = kv[0].trim().replace("\"", "");
//                 String val = kv[1].trim().replace("\"", "");
//                 fieldNames.add(key);
//                 try { pctValues.add(Integer.parseInt(val)); }
//                 catch (NumberFormatException e) { pctValues.add(0); }
//             }
//         }

//         if (fieldNames.isEmpty()) return;

//         PdfPTable table = new PdfPTable(fieldNames.size());
//         table.setWidthPercentage(100);
//         table.setSpacingBefore(4);

//         for (int i = 0; i < fieldNames.size(); i++) {
//             String key = fieldNames.get(i);
//             int    pct = pctValues.get(i);

//             PdfPCell cell = new PdfPCell();
//             cell.setBackgroundColor(COLOR_SECTION_BG);
//             cell.setBorderColor(COLOR_BORDER);
//             cell.setBorderWidth(0.5f);
//             cell.setPadding(10);
//             cell.setHorizontalAlignment(Element.ALIGN_CENTER);

//             Paragraph scoreP = new Paragraph(String.valueOf(pct),
//                     new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, scoreColor(pct)));
//             scoreP.setAlignment(Element.ALIGN_CENTER);
//             cell.addElement(scoreP);

//             Paragraph barHolder = new Paragraph();
//             barHolder.setSpacingBefore(4);
//             cell.addElement(barHolder);
//             cell.addElement(buildProgressBar(pct));

//             Paragraph labelP = new Paragraph(toTitleCase(key.replace("_", " ")), FONT_LABEL);
//             labelP.setAlignment(Element.ALIGN_CENTER);
//             labelP.setSpacingBefore(4);
//             cell.addElement(labelP);

//             table.addCell(cell);
//         }

//         doc.add(table);
//     }

//     // =========================================================================
//     // SECTION: SECTION HEADER
//     // =========================================================================

//     private void addSectionHeader(Document doc, String text, BaseColor color) throws Exception {
//         PdfPTable header = new PdfPTable(1);
//         header.setWidthPercentage(100);
//         header.setSpacingBefore(12);
//         header.setSpacingAfter(6);

//         PdfPCell cell = new PdfPCell(
//                 new Phrase(text, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, color)));
//         cell.setBackgroundColor(COLOR_SECTION_BG);
//         cell.setBorderWidthLeft(4f);
//         cell.setBorderColorLeft(color);
//         cell.setBorderWidthTop(0);
//         cell.setBorderWidthRight(0);
//         cell.setBorderWidthBottom(0.5f);
//         cell.setBorderColorBottom(COLOR_BORDER);
//         cell.setPaddingTop(8);
//         cell.setPaddingBottom(8);
//         cell.setPaddingLeft(12);
//         cell.setPaddingRight(8);
//         header.addCell(cell);

//         doc.add(header);
//     }

//     // =========================================================================
//     // SECTION: SKILL ROW  (progress bar card)
//     // =========================================================================

//     private void addSkillRow(Document doc, JsonNode skill, BaseColor accentColor) throws Exception {
//         String skillName = skill.path("skill_name").asText("N/A");
//         String desc      = skill.path("description").asText("");
//         int    pct       = skill.path("percentage").asInt(0);
//         String level     = getLevel(pct);

//         PdfPTable card = new PdfPTable(1);
//         card.setWidthPercentage(100);
//         card.setSpacingBefore(4);

//         PdfPCell cell = new PdfPCell();
//         cell.setBackgroundColor(BaseColor.WHITE);
//         cell.setBorderColor(COLOR_BORDER);
//         cell.setBorderWidth(0.5f);
//         cell.setBorderWidthLeft(3f);
//         cell.setBorderColorLeft(accentColor);
//         cell.setPaddingTop(10);
//         cell.setPaddingBottom(10);
//         cell.setPaddingLeft(12);
//         cell.setPaddingRight(12);

//         // Top row: name | level | pct
//         PdfPTable topRow = new PdfPTable(3);
//         topRow.setWidths(new float[]{4f, 1.4f, 0.9f});
//         topRow.setWidthPercentage(100);

//         PdfPCell nameCell = new PdfPCell(new Phrase(skillName, FONT_SKILL_NAME));
//         nameCell.setBorder(Rectangle.NO_BORDER);
//         nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//         topRow.addCell(nameCell);

//         PdfPCell lvlCell = new PdfPCell(new Phrase(level, FONT_SKILL_LVL));
//         lvlCell.setBorder(Rectangle.NO_BORDER);
//         lvlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//         lvlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//         topRow.addCell(lvlCell);

//         PdfPCell pctCell = new PdfPCell(new Phrase(pct + "/100", FONT_SKILL_PCT));
//         pctCell.setBorder(Rectangle.NO_BORDER);
//         pctCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//         pctCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//         topRow.addCell(pctCell);

//         cell.addElement(topRow);
//         cell.addElement(buildProgressBar(pct));

//         if (!desc.isEmpty()) {
//             Paragraph descPara = new Paragraph(desc, FONT_BODY);
//             descPara.setSpacingBefore(5);
//             descPara.setLeading(16f);
//             cell.addElement(descPara);
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

//     // =========================================================================
//     // SECTION: PROGRESS BAR
//     // =========================================================================

//     private PdfPTable buildProgressBar(int pct) throws Exception {
//         int filled = Math.max(0, Math.min(100, pct));
//         int empty  = 100 - filled;

//         if (filled == 0) {
//             PdfPTable bar = new PdfPTable(1);
//             bar.setWidthPercentage(100);
//             bar.setSpacingBefore(4);
//             PdfPCell c = new PdfPCell(new Phrase(" "));
//             c.setFixedHeight(5f);
//             c.setBackgroundColor(COLOR_BAR_BG);
//             c.setBorder(Rectangle.NO_BORDER);
//             bar.addCell(c);
//             return bar;
//         }

//         if (empty == 0) {
//             PdfPTable bar = new PdfPTable(1);
//             bar.setWidthPercentage(100);
//             bar.setSpacingBefore(4);
//             PdfPCell c = new PdfPCell(new Phrase(" "));
//             c.setFixedHeight(5f);
//             c.setBackgroundColor(scoreColor(pct));
//             c.setBorder(Rectangle.NO_BORDER);
//             bar.addCell(c);
//             return bar;
//         }

//         PdfPTable bar = new PdfPTable(2);
//         bar.setWidths(new float[]{filled, empty});
//         bar.setWidthPercentage(100);
//         bar.setSpacingBefore(4);

//         PdfPCell filledCell = new PdfPCell(new Phrase(" "));
//         filledCell.setFixedHeight(5f);
//         filledCell.setBackgroundColor(scoreColor(pct));
//         filledCell.setBorder(Rectangle.NO_BORDER);
//         bar.addCell(filledCell);

//         PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
//         emptyCell.setFixedHeight(5f);
//         emptyCell.setBackgroundColor(COLOR_BAR_BG);
//         emptyCell.setBorder(Rectangle.NO_BORDER);
//         bar.addCell(emptyCell);

//         return bar;
//     }

//     // =========================================================================
//     // SECTION: BULLET ITEM  (Strengths & Improvements — identical style)
//     // =========================================================================

//     /**
//      * Renders one bullet card with a coloured left border, heading in bold,
//      * and an optional description in muted text — same layout for both
//      * Top Strengths and Areas for Improvement.
//      */
//     private void addBulletItem(Document doc, String heading, String body,
//                                 BaseColor borderColor, BaseColor bgColor) throws Exception {
//         if (heading == null || heading.isBlank()) return;

//         PdfPTable card = new PdfPTable(1);
//         card.setWidthPercentage(100);
//         card.setSpacingBefore(4);

//         PdfPCell cell = new PdfPCell();
//         cell.setBorderWidthLeft(3f);
//         cell.setBorderColorLeft(borderColor);
//         cell.setBorderWidthTop(0);
//         cell.setBorderWidthRight(0);
//         cell.setBorderWidthBottom(0.5f);
//         cell.setBorderColorBottom(COLOR_BORDER);
//         cell.setBackgroundColor(bgColor);
//         cell.setPaddingTop(8);
//         cell.setPaddingBottom(8);
//         cell.setPaddingLeft(12);
//         cell.setPaddingRight(12);

//         Paragraph headPara = new Paragraph("\u2022  " + heading, FONT_BULLET);
//         cell.addElement(headPara);

//         if (body != null && !body.isBlank()) {
//             Paragraph bodyPara = new Paragraph(body, FONT_BODY);
//             bodyPara.setIndentationLeft(14);
//             bodyPara.setSpacingBefore(3);
//             bodyPara.setLeading(16f);
//             cell.addElement(bodyPara);
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

//     // =========================================================================
//     // SECTION: INTERVIEW Q&A BLOCK
//     // =========================================================================

//     private void addQuestionBlock(Document doc, JsonNode q) throws Exception {
//         String question = q.path("question").asText("");
//         String response = q.path("response_summary").asText("");
//         int    score    = q.path("score").asInt(0);

//         PdfPTable card = new PdfPTable(1);
//         card.setWidthPercentage(100);
//         card.setSpacingBefore(6);

//         PdfPCell cell = new PdfPCell();
//         cell.setBackgroundColor(BaseColor.WHITE);
//         cell.setBorderColor(COLOR_BORDER);
//         cell.setBorderWidth(0.5f);
//         cell.setBorderWidthLeft(3f);
//         cell.setBorderColorLeft(COLOR_PRIMARY);
//         cell.setPaddingTop(10);
//         cell.setPaddingBottom(10);
//         cell.setPaddingLeft(12);
//         cell.setPaddingRight(12);

//         // Q + score on same line
//         PdfPTable qRow = new PdfPTable(2);
//         qRow.setWidths(new float[]{5f, 1f});
//         qRow.setWidthPercentage(100);

//         PdfPCell qCell = new PdfPCell(new Phrase("Q: " + question, FONT_Q));
//         qCell.setBorder(Rectangle.NO_BORDER);
//         qRow.addCell(qCell);

//         BaseColor scoreCol = score >= 60 ? COLOR_SUCCESS
//                            : score >= 30 ? COLOR_WARNING
//                                          : COLOR_DANGER;
//         PdfPCell scoreCell = new PdfPCell(new Phrase("Score: " + score + "/100",
//                 new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, scoreCol)));
//         scoreCell.setBorder(Rectangle.NO_BORDER);
//         scoreCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//         scoreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//         qRow.addCell(scoreCell);

//         cell.addElement(qRow);
//         cell.addElement(buildProgressBar(score));

//         if (!response.isEmpty()) {
//             Paragraph resp = new Paragraph("Response Summary:  " + response, FONT_Q_BODY);
//             resp.setSpacingBefore(7);
//             resp.setLeading(16f);
//             cell.addElement(resp);
//         }

//         JsonNode insights = q.path("key_insights");
//         if (insights.isArray() && insights.size() > 0) {
//             Paragraph insightsTitle = new Paragraph("Key Insights:",
//                     new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_TEXT_MUTED));
//             insightsTitle.setSpacingBefore(7);
//             cell.addElement(insightsTitle);
//             for (JsonNode insight : insights) {
//                 Paragraph ins = new Paragraph("  \u2022  " + insight.asText(), FONT_BODY);
//                 ins.setSpacingBefore(2);
//                 cell.addElement(ins);
//             }
//         }

//         JsonNode missed = q.path("missed_opportunities");
//         if (missed.isArray() && missed.size() > 0) {
//             Paragraph missedTitle = new Paragraph("Missed Opportunities:",
//                     new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_DANGER));
//             missedTitle.setSpacingBefore(6);
//             cell.addElement(missedTitle);
//             for (JsonNode m : missed) {
//                 Paragraph mp = new Paragraph("  \u2022  " + m.asText(), FONT_BODY);
//                 mp.setSpacingBefore(2);
//                 cell.addElement(mp);
//             }
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

//     // =========================================================================
//     // UTILITIES
//     // =========================================================================

//     private void addSpacer(Document doc) throws Exception {
//         doc.add(Chunk.NEWLINE);
//     }

//     private BaseColor scoreColor(int pct) {
//         if (pct >= 70) return COLOR_SUCCESS;
//         if (pct >= 40) return COLOR_WARNING;
//         return COLOR_DANGER;
//     }

//     private String getLevel(int pct) {
//         if (pct >= 80) return "Advanced";
//         if (pct >= 60) return "Intermediate";
//         if (pct >= 30) return "Basic";
//         return "Not Demonstrated";
//     }

//     private String toTitleCase(String input) {
//         if (input == null || input.isEmpty()) return input;
//         String[] words = input.split(" ");
//         StringBuilder sb = new StringBuilder();
//         for (String word : words) {
//             if (!word.isEmpty()) {
//                 sb.append(Character.toUpperCase(word.charAt(0)))
//                   .append(word.substring(1).toLowerCase())
//                   .append(" ");
//             }
//         }
//         return sb.toString().trim();
//     }
// }


