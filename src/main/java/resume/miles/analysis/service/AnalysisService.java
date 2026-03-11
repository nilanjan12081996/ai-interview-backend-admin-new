// package resume.miles.analysis.service;

// import java.io.File;
// import java.io.FileOutputStream;
// import java.util.ArrayList;

// import org.springframework.stereotype.Service;

// // import com.itextpdf.text.BaseColor;
// // import com.itextpdf.text.Chunk;
// // import com.itextpdf.text.Document;
// // import com.itextpdf.text.Font;
// // import com.itextpdf.text.PageSize;
// // import com.itextpdf.text.Paragraph;
// // import com.itextpdf.text.Phrase;
// // import com.itextpdf.text.pdf.PdfContentByte;
// // import com.itextpdf.text.pdf.PdfPCell;
// // import com.itextpdf.text.pdf.PdfPTable;
// // import com.itextpdf.text.pdf.PdfWriter;

// // import com.itextpdf.text.*;
// // import com.itextpdf.text.pdf.*;

// import com.itextpdf.text.BaseColor;
// import com.itextpdf.text.Chunk;
// import com.itextpdf.text.Document;
// import com.itextpdf.text.Element;
// import com.itextpdf.text.Font;
// import com.itextpdf.text.PageSize;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.Phrase;
// import com.itextpdf.text.Rectangle;
// import com.itextpdf.text.pdf.PdfPCell;
// import com.itextpdf.text.pdf.PdfPTable;
// import com.itextpdf.text.pdf.PdfWriter;
// import java.util.ArrayList;
// import java.util.List;
// import tools.jackson.databind.JsonNode;
// import java.util.Iterator;
// import java.util.Map;

// import lombok.RequiredArgsConstructor;
// import resume.miles.analysis.dto.AnalysisRequestDto;
// import resume.miles.analysis.entity.AnalysisEntity;
// import resume.miles.analysis.repository.AnalysisRepository;
// import resume.miles.interview.entity.InterviewLinkEntity;
// import resume.miles.interview.repository.InterviewLinkRepository;
// import resume.miles.transcription.entity.TranscriptionEntity;
// import tools.jackson.databind.ObjectMapper;

// @Service
// @RequiredArgsConstructor
// public class  AnalysisService{
//     private final InterviewLinkRepository interviewLinkRepository;
//     private final AnalysisRepository analysisRepository;
//     private static final String BASE_DIR = "uploads/analysis/";


//     private static final BaseColor COLOR_PRIMARY     = new BaseColor(67, 97, 238);   // #4361EE blue
//     private static final BaseColor COLOR_SUCCESS     = new BaseColor(34, 197, 94);   // #22C55E green
//     private static final BaseColor COLOR_WARNING     = new BaseColor(234, 179, 8);   // #EAB308 yellow
//     private static final BaseColor COLOR_DANGER      = new BaseColor(239, 68, 68);   // #EF4444 red
//     private static final BaseColor COLOR_BG_LIGHT    = new BaseColor(248, 249, 255); // near-white blue tint
//     private static final BaseColor COLOR_BORDER      = new BaseColor(226, 232, 240); // #E2E8F0
//     private static final BaseColor COLOR_TEXT_DARK   = new BaseColor(15, 23, 42);    // #0F172A
//     private static final BaseColor COLOR_TEXT_MUTED  = new BaseColor(100, 116, 139); // #64748B
//     private static final BaseColor COLOR_SECTION_BG  = new BaseColor(239, 246, 255); // #EFF6FF
//     private static final BaseColor COLOR_BAR_BG      = new BaseColor(226, 232, 240); // progress bar track

//     // ─── Fonts ───────────────────────────────────────────────────────────────
//     private static final Font FONT_TITLE     = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_SUBTITLE  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, new BaseColor(200, 220, 255));
//     private static final Font FONT_NAME      = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_SECTION   = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,   COLOR_TEXT_DARK);
//     private static final Font FONT_LABEL     = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   COLOR_TEXT_MUTED);
//     private static final Font FONT_VALUE     = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT_DARK);
//     private static final Font FONT_SKILL_NAME= new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_TEXT_DARK);
//     private static final Font FONT_SKILL_LVL = new Font(Font.FontFamily.HELVETICA, 9,  Font.ITALIC, COLOR_TEXT_MUTED);
//     private static final Font FONT_SKILL_PCT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_PRIMARY);
//     private static final Font FONT_BODY      = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, COLOR_TEXT_MUTED);
//     private static final Font FONT_BULLET    = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT_DARK);
//     private static final Font FONT_SCORE_NUM = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD,   BaseColor.WHITE);
//     private static final Font FONT_SCORE_LBL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(200, 220, 255));
//     private static final Font FONT_Q        = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_PRIMARY);
//     private static final Font FONT_Q_BODY   = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, COLOR_TEXT_DARK);
//     private static final Font FONT_Q_SCORE  = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   COLOR_SUCCESS);


//     public void saveAnalysis(AnalysisRequestDto request){
//         InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
//             .orElseThrow(() -> new RuntimeException("Invalid token"));

//     AnalysisEntity analysis = AnalysisEntity.builder()
//             .interviewLinkId(interviewLink.getId())
//             .analysis(request.getAnalysis())
//             .status(1)
//             .build();

//     analysisRepository.save(analysis);
//     }



//   public String generateAnalysisPdf(Long interviewId, String analysisJson) {
//         return generateAnalysisPdf(interviewId, analysisJson, null, null, null, null, null, null, null, null);
//     }

//     /**
//      * Call this overload from InterviewService:
//      *
//      *   analysisFileLink = analysisService.generateAnalysisPdf(
//      *       link.getId(),
//      *       analysis.get().getAnalysis(),
//      *       interview.getCandidateName(),
//      *       interview.getEmail(),
//      *       interview.getPhoneNumber(),
//      *       link.getInterviewLink(),
//      *       interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null
//      *   );
//      */
//     public String generateAnalysisPdf(
//            Long   interviewId,
//         String analysisJson,
//         String candidateName,
//         String candidateEmail,
//         String candidatePhone,
//         String interviewLink,
//         String interviewDate,
//         String interviewDuration,   // ← NEW
//         String videoLink,           // ← NEW
//         String transcriptionLink    // ← NEW

//     ) {
//         try {
//             File directory = new File(BASE_DIR);
//             if (!directory.exists()) directory.mkdirs();

//             String fileName = "analysis_" + interviewId + ".pdf";
//             File   file     = new File(BASE_DIR + fileName);

//             if (file.exists()) {
//                 return "/uploads/analysis/" + fileName;
//             }

//             // Parse JSON
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

//             // Build PDF
//             Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
//             PdfWriter.getInstance(doc, new FileOutputStream(file));
//             doc.open();

//             addHeaderBanner(doc, candidateName, candidateEmail, candidatePhone,
//                     analysis.path("overall_score").asText("N/A"));
//             doc.add(Chunk.NEWLINE);

//             // addCandidateInfoTable(doc, candidateName, candidateEmail, candidatePhone, interviewLink, interviewDate);
//             addCandidateInfoTable(doc, candidateName, candidateEmail, candidatePhone,
//         interviewLink, interviewDate, interviewDuration, videoLink, transcriptionLink);
//             doc.add(Chunk.NEWLINE);

//             JsonNode catScores = analysis.path("category_scores");
//             if (!catScores.isMissingNode() && !catScores.isEmpty()) {
//                 addCategoryScores(doc, catScores);
//                 doc.add(Chunk.NEWLINE);
//             }

//             addSectionHeader(doc, "\u2713  Must Have Skills", COLOR_SUCCESS);
//             JsonNode mustSkills = analysis.path("must_to_have_skills");
//             if (mustSkills.isArray()) {
//                 for (JsonNode skill : mustSkills) addSkillRow(doc, skill);
//             }
//             doc.add(Chunk.NEWLINE);

//             addSectionHeader(doc, "\u2605  Good to Have Skills", COLOR_WARNING);
//             JsonNode goodSkills = analysis.path("good_to_have_skills");
//             if (goodSkills.isArray()) {
//                 for (JsonNode skill : goodSkills) addSkillRow(doc, skill);
//             }
//             doc.add(Chunk.NEWLINE);

//             addStrengthsWeaknesses(doc, analysis);
//             doc.add(Chunk.NEWLINE);

//             JsonNode improvements = analysis.path("areas_for_improvement");
//             if (improvements.isArray() && improvements.size() > 0) {
//                 addSectionHeader(doc, "\u26A0  Areas for Improvement", COLOR_DANGER);
//                 for (JsonNode area : improvements) {
//                     addBulletItem(doc,
//                             area.path("area").asText(""),
//                             area.path("description").asText(""),
//                             COLOR_DANGER);
//                 }
//                 doc.add(Chunk.NEWLINE);
//             }

//             JsonNode questions = analysis.path("interview_questions_responses");
//             if (questions.isArray() && questions.size() > 0) {
//                 addSectionHeader(doc, "\u2753  Interview Questions & Responses", COLOR_PRIMARY);
//                 for (JsonNode q : questions) addQuestionBlock(doc, q);
//                 doc.add(Chunk.NEWLINE);
//             }

//             addSectionHeader(doc, "Overall Interview Summary", COLOR_PRIMARY);
//             PdfPTable summaryBox = new PdfPTable(1);
//             summaryBox.setWidthPercentage(100);
//             PdfPCell summaryCell = new PdfPCell();
//             summaryCell.setBackgroundColor(COLOR_SECTION_BG);
//             summaryCell.setBorderColor(COLOR_PRIMARY);
//             summaryCell.setBorderWidth(1f);
//             summaryCell.setPadding(12);
//             summaryCell.addElement(new Paragraph(
//                     analysis.path("overall_ai_summary").asText("N/A"), FONT_Q_BODY));
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
//     // PRIVATE HELPERS
//     // =========================================================================

//     private void addHeaderBanner(Document doc,
//                                   String name, String email, String phone,
//                                   String score) throws Exception {
//         PdfPTable banner = new PdfPTable(2);
//         banner.setWidthPercentage(100);
//         banner.setWidths(new float[]{3f, 1f});

//         PdfPCell left = new PdfPCell();
//         left.setBackgroundColor(COLOR_PRIMARY);
//         left.setBorder(Rectangle.NO_BORDER);
//         left.setPadding(16);
//         left.addElement(new Paragraph("INTERVIEW ANALYSIS REPORT", FONT_TITLE));

//         String displayName = (name != null && !name.isEmpty()) ? name : "Candidate";
//         Paragraph namePara = new Paragraph(displayName, FONT_NAME);
//         namePara.setSpacingBefore(4);
//         left.addElement(namePara);

//         if (email != null && !email.isEmpty()) {
//             String contact = email + (phone != null && !phone.isEmpty() ? "  |  " + phone : "");
//             left.addElement(new Paragraph(contact, FONT_SUBTITLE));
//         }
//         banner.addCell(left);

//         PdfPCell right = new PdfPCell();
//         right.setBackgroundColor(new BaseColor(45, 75, 200));
//         right.setBorder(Rectangle.NO_BORDER);
//         right.setPadding(16);
//         right.setHorizontalAlignment(Element.ALIGN_CENTER);
//         right.setVerticalAlignment(Element.ALIGN_MIDDLE);

//         Paragraph scoreNum = new Paragraph(score + "/100", FONT_SCORE_NUM);
//         scoreNum.setAlignment(Element.ALIGN_CENTER);
//         right.addElement(scoreNum);

//         Paragraph scoreLbl = new Paragraph("Overall Score", FONT_SCORE_LBL);
//         scoreLbl.setAlignment(Element.ALIGN_CENTER);
//         right.addElement(scoreLbl);
//         banner.addCell(right);

//         doc.add(banner);
//     }

//     // private void addCandidateInfoTable(Document doc,
//     //                                     String name, String email,
//     //                                     String phone, String link,
//     //                                     String date) throws Exception {
//     //     PdfPTable table = new PdfPTable(4);
//     //     table.setWidthPercentage(100);
//     //     table.setWidths(new float[]{1f, 1f, 1f, 1f});
//     //     table.setSpacingBefore(8);

//     //     addInfoCell(table, "Candidate",     name  != null ? name  : "\u2014");
//     //     addInfoCell(table, "Email",         email != null ? email : "\u2014");
//     //     addInfoCell(table, "Phone",         phone != null ? phone : "\u2014");
//     //     addInfoCell(table, "Interview URL", link  != null ? link  : "\u2014");

//     //     doc.add(table);
//     // }


//     private void addCandidateInfoTable(Document doc,
//                                     String name, String email,
//                                     String phone, String interviewLink,
//                                     String date,  String duration,
//                                     String videoLink, String transcriptionLink) throws Exception {

//     PdfPTable wrapper = new PdfPTable(1);
//     wrapper.setWidthPercentage(100);
//     wrapper.setSpacingBefore(8);

//     PdfPCell outerCell = new PdfPCell();
//     outerCell.setBackgroundColor(COLOR_BG_LIGHT);
//     outerCell.setBorderColor(COLOR_BORDER);
//     outerCell.setBorderWidth(0.5f);
//     outerCell.setPadding(12);

//     // Helper: reusable row builder
//     // Label row
//     outerCell.addElement(makeLabelValueRow("Candidate Name", name));
//     outerCell.addElement(makeLabelValueRow("Email ID", email));
//     outerCell.addElement(makeLabelValueRow("Phone", phone));
//     outerCell.addElement(makeLabelValueRow("Interview Date", date));
//     outerCell.addElement(makeLabelValueRow("Interview Duration", duration));
//     outerCell.addElement(makeLinkRow("Interview Link", interviewLink));
//     outerCell.addElement(makeLinkRow("Video Link", videoLink));
//     outerCell.addElement(makeLinkRow("Transcription Link", transcriptionLink));

//     wrapper.addCell(outerCell);
//     doc.add(wrapper);
// }

// /** Plain text label: value row */
// private Paragraph makeLabelValueRow(String label, String value) {
//     String display = (value != null && !value.isEmpty()) ? value : "\u2014";
//     Paragraph p = new Paragraph();
//     p.setSpacingBefore(4);
//     p.add(new Chunk(label + ":  ", FONT_LABEL));
//     p.add(new Chunk(display, FONT_VALUE));
//     return p;
// }

// /** Anchor/link row — value is rendered as a blue underlined link */
// private Paragraph makeLinkRow(String label, String url) throws Exception {
//     Paragraph p = new Paragraph();
//     p.setSpacingBefore(4);
//     p.add(new Chunk(label + ":  ", FONT_LABEL));

//     if (url != null && !url.isEmpty()) {
//         Font linkFont = new Font(Font.FontFamily.HELVETICA, 10, Font.UNDERLINE, COLOR_PRIMARY);
//         Chunk link = new Chunk(url, linkFont);
//         link.setAnchor(url);           // makes it a real clickable hyperlink in PDF
//         p.add(link);
//     } else {
//         p.add(new Chunk("\u2014", FONT_VALUE));
//     }
//     return p;
// }

//     private void addInfoCell(PdfPTable table, String label, String value) {
//         PdfPCell cell = new PdfPCell();
//         cell.setBackgroundColor(COLOR_BG_LIGHT);
//         cell.setBorderColor(COLOR_BORDER);
//         cell.setPadding(8);
//         cell.addElement(new Paragraph(label.toUpperCase(), FONT_LABEL));
//         String display = value.length() > 40 ? value.substring(0, 37) + "..." : value;
//         cell.addElement(new Paragraph(display, FONT_VALUE));
//         table.addCell(cell);
//     }

//     private void addCategoryScores(Document doc, JsonNode catScores) throws Exception {
//         addSectionHeader(doc, "Category Scores", COLOR_PRIMARY);

//         // Serialize to string and parse manually — works with any Jackson version
//         ObjectMapper mapper = new ObjectMapper();
//         String json = mapper.writeValueAsString(catScores).trim();
//         if (json.startsWith("{")) json = json.substring(1);
//         if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);

//         List<String> fieldNames = new ArrayList<>();
//         List<Integer> pctValues  = new ArrayList<>();

//         String[] pairs = json.split(",");
//         for (String pair : pairs) {
//             String[] kv = pair.split(":");
//             if (kv.length == 2) {
//                 String key = kv[0].trim().replace("\"", "");
//                 String val = kv[1].trim().replace("\"", "");
//                 fieldNames.add(key);
//                 try {
//                     pctValues.add(Integer.parseInt(val));
//                 } catch (NumberFormatException e) {
//                     pctValues.add(0);
//                 }
//             }
//         }

//         if (fieldNames.isEmpty()) return;

//         PdfPTable table = new PdfPTable(fieldNames.size());
//         table.setWidthPercentage(100);

//         for (int i = 0; i < fieldNames.size(); i++) {
//             String key = fieldNames.get(i);
//             int    pct = pctValues.get(i);

//             PdfPCell cell = new PdfPCell();
//             cell.setBackgroundColor(COLOR_SECTION_BG);
//             cell.setBorderColor(COLOR_BORDER);
//             cell.setPadding(10);
//             cell.setHorizontalAlignment(Element.ALIGN_CENTER);

//             Paragraph scoreP = new Paragraph(String.valueOf(pct),
//                     new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, scoreColor(pct)));
//             scoreP.setAlignment(Element.ALIGN_CENTER);
//             cell.addElement(scoreP);

//             Paragraph labelP = new Paragraph(toTitleCase(key.replace("_", " ")), FONT_LABEL);
//             labelP.setAlignment(Element.ALIGN_CENTER);
//             cell.addElement(labelP);

//             table.addCell(cell);
//         }

//         doc.add(table);
//     }

//     private void addSectionHeader(Document doc, String text, BaseColor color) throws Exception {
//         PdfPTable header = new PdfPTable(1);
//         header.setWidthPercentage(100);
//         header.setSpacingBefore(10);
//         header.setSpacingAfter(6);

//         PdfPCell cell = new PdfPCell(
//                 new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, color)));
//         cell.setBackgroundColor(COLOR_SECTION_BG);
//         cell.setBorderWidthLeft(4f);
//         cell.setBorderColorLeft(color);
//         cell.setBorderWidthTop(0);
//         cell.setBorderWidthRight(0);
//         cell.setBorderWidthBottom(0);
//         cell.setPadding(8);
//         header.addCell(cell);

//         doc.add(header);
//     }

//     private void addSkillRow(Document doc, JsonNode skill) throws Exception {
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
//         cell.setPadding(10);

//         PdfPTable topRow = new PdfPTable(3);
//         topRow.setWidths(new float[]{4f, 1.2f, 0.8f});
//         topRow.setWidthPercentage(100);

//         PdfPCell nameCell = new PdfPCell(new Phrase(skillName, FONT_SKILL_NAME));
//         nameCell.setBorder(Rectangle.NO_BORDER);
//         topRow.addCell(nameCell);

//         PdfPCell lvlCell = new PdfPCell(new Phrase(level, FONT_SKILL_LVL));
//         lvlCell.setBorder(Rectangle.NO_BORDER);
//         lvlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//         topRow.addCell(lvlCell);

//         PdfPCell pctCell = new PdfPCell(new Phrase(pct + "/100", FONT_SKILL_PCT));
//         pctCell.setBorder(Rectangle.NO_BORDER);
//         pctCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//         topRow.addCell(pctCell);

//         cell.addElement(topRow);
//         cell.addElement(buildProgressBar(pct));

//         if (!desc.isEmpty()) {
//             Paragraph descPara = new Paragraph(desc, FONT_BODY);
//             descPara.setSpacingBefore(4);
//             cell.addElement(descPara);
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

//     private PdfPTable buildProgressBar(int pct) throws Exception {
//         int filled = Math.max(0, Math.min(100, pct));
//         int empty  = 100 - filled;

//         if (filled == 0) {
//             PdfPTable bar = new PdfPTable(1);
//             bar.setWidthPercentage(100);
//             PdfPCell c = new PdfPCell(new Phrase(" "));
//             c.setFixedHeight(6f);
//             c.setBackgroundColor(COLOR_BAR_BG);
//             c.setBorder(Rectangle.NO_BORDER);
//             bar.addCell(c);
//             return bar;
//         }

//         if (empty == 0) {
//             PdfPTable bar = new PdfPTable(1);
//             bar.setWidthPercentage(100);
//             PdfPCell c = new PdfPCell(new Phrase(" "));
//             c.setFixedHeight(6f);
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
//         filledCell.setFixedHeight(6f);
//         filledCell.setBackgroundColor(scoreColor(pct));
//         filledCell.setBorder(Rectangle.NO_BORDER);
//         bar.addCell(filledCell);

//         PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
//         emptyCell.setFixedHeight(6f);
//         emptyCell.setBackgroundColor(COLOR_BAR_BG);
//         emptyCell.setBorder(Rectangle.NO_BORDER);
//         bar.addCell(emptyCell);

//         return bar;
//     }

//     // private void addStrengthsWeaknesses(Document doc, JsonNode analysis) throws Exception {
//     //     PdfPTable swTable = new PdfPTable(2);
//     //     swTable.setWidthPercentage(100);
//     //     swTable.setWidths(new float[]{1f, 1f});
//     //     swTable.setSpacingBefore(8);

//     //     // Strengths
//     //     PdfPCell strengthsCell = new PdfPCell();
//     //     strengthsCell.setBorderColor(COLOR_BORDER);
//     //     strengthsCell.setPadding(12);
//     //     strengthsCell.setBackgroundColor(new BaseColor(240, 253, 244));
//     //     strengthsCell.addElement(new Paragraph("\u2605  Strengths",
//     //             new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_SUCCESS)));
//     //     strengthsCell.addElement(new Paragraph(" "));

//     //     JsonNode strengths = analysis.path("top_strengths");
//     //     if (strengths.isArray()) {
//     //         for (JsonNode s : strengths) {
//     //             Paragraph bullet = new Paragraph("\u2022  " + s.asText(""), FONT_BULLET);
//     //             bullet.setSpacingBefore(3);
//     //             strengthsCell.addElement(bullet);
//     //         }
//     //     }
//     //     swTable.addCell(strengthsCell);

//     //     // Weaknesses
//     //     PdfPCell weakCell = new PdfPCell();
//     //     weakCell.setBorderColor(COLOR_BORDER);
//     //     weakCell.setPadding(12);
//     //     weakCell.setBackgroundColor(new BaseColor(255, 241, 242));
//     //     weakCell.addElement(new Paragraph("\u26A0  Weaknesses",
//     //             new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_DANGER)));
//     //     weakCell.addElement(new Paragraph(" "));

//     //     JsonNode improvements = analysis.path("areas_for_improvement");
//     //     if (improvements.isArray()) {
//     //         for (JsonNode area : improvements) {
//     //             Paragraph bullet = new Paragraph(
//     //                     "\u2022  " + area.path("area").asText(""), FONT_BULLET);
//     //             bullet.setSpacingBefore(3);
//     //             weakCell.addElement(bullet);
//     //         }
//     //     }
//     //     swTable.addCell(weakCell);

//     //     doc.add(swTable);
//     // }


//     private void addStrengthsWeaknesses(Document doc, JsonNode analysis) throws Exception {

//     // ── Strengths ──────────────────────────────────────────────────────────
//     JsonNode strengths = analysis.path("top_strengths");
//     if (strengths.isArray() && strengths.size() > 0) {
//         addSectionHeader(doc, "\u2605  Top Strengths", COLOR_SUCCESS);
//         for (JsonNode s : strengths) {
//             // top_strengths is usually a plain string array — no sub-description
//             addBulletItem(doc,
//                     s.path("area").isMissingNode()
//                             ? s.asText("")                        // plain string
//                             : s.path("area").asText(""),          // object with area key
//                     s.path("description").asText(""),
//                     COLOR_SUCCESS);
//         }
//         doc.add(Chunk.NEWLINE);
//     }

//     // ── Weaknesses ─────────────────────────────────────────────────────────
//     JsonNode improvements = analysis.path("areas_for_improvement");
//     if (improvements.isArray() && improvements.size() > 0) {
//         addSectionHeader(doc, "\u26A0  Weaknesses / Areas for Improvement", COLOR_DANGER);
//         for (JsonNode area : improvements) {
//             addBulletItem(doc,
//                     area.path("area").asText(""),
//                     area.path("description").asText(""),
//                     COLOR_DANGER);
//         }
//         doc.add(Chunk.NEWLINE);
//     }
// }

//     private void addBulletItem(Document doc, String heading, String body,
//                                 BaseColor color) throws Exception {
//         PdfPTable card = new PdfPTable(1);
//         card.setWidthPercentage(100);
//         card.setSpacingBefore(3);

//         PdfPCell cell = new PdfPCell();
//         cell.setBorderWidthLeft(3f);
//         cell.setBorderColorLeft(color);
//         cell.setBorderWidthTop(0);
//         cell.setBorderWidthRight(0);
//         cell.setBorderWidthBottom(0);
//         cell.setBackgroundColor(COLOR_BG_LIGHT);
//         cell.setPadding(8);

//         if (!heading.isEmpty()) {
//             cell.addElement(new Paragraph("\u2022  " + heading,
//                     new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, COLOR_TEXT_DARK)));
//         }
//         if (!body.isEmpty()) {
//             Paragraph bodyP = new Paragraph(body, FONT_BODY);
//             bodyP.setIndentationLeft(12);
//             cell.addElement(bodyP);
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

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
//         cell.setPadding(10);

//         PdfPTable qRow = new PdfPTable(2);
//         qRow.setWidths(new float[]{5f, 1f});
//         qRow.setWidthPercentage(100);

//         PdfPCell qCell = new PdfPCell(new Phrase("Q: " + question, FONT_Q));
//         qCell.setBorder(Rectangle.NO_BORDER);
//         qRow.addCell(qCell);

//         BaseColor scoreCol = score >= 60 ? COLOR_SUCCESS
//                            : score >= 30 ? COLOR_WARNING
//                            : COLOR_DANGER;
//         PdfPCell scoreCell = new PdfPCell(new Phrase("Score: " + score,
//                 new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, scoreCol)));
//         scoreCell.setBorder(Rectangle.NO_BORDER);
//         scoreCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//         qRow.addCell(scoreCell);

//         cell.addElement(qRow);
//         cell.addElement(buildProgressBar(score));

//         if (!response.isEmpty()) {
//             Paragraph resp = new Paragraph("Response Summary: " + response, FONT_Q_BODY);
//             resp.setSpacingBefore(6);
//             cell.addElement(resp);
//         }

//         JsonNode insights = q.path("key_insights");
//         if (insights.isArray() && insights.size() > 0) {
//             Paragraph insightsTitle = new Paragraph("Key Insights:",
//                     new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_TEXT_MUTED));
//             insightsTitle.setSpacingBefore(6);
//             cell.addElement(insightsTitle);
//             for (JsonNode insight : insights) {
//                 cell.addElement(new Paragraph("  \u2022  " + insight.asText(), FONT_BODY));
//             }
//         }

//         JsonNode missed = q.path("missed_opportunities");
//         if (missed.isArray() && missed.size() > 0) {
//             Paragraph missedTitle = new Paragraph("Missed Opportunities:",
//                     new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_DANGER));
//             missedTitle.setSpacingBefore(4);
//             cell.addElement(missedTitle);
//             for (JsonNode m : missed) {
//                 cell.addElement(new Paragraph("  \u2022  " + m.asText(), FONT_BODY));
//             }
//         }

//         card.addCell(cell);
//         doc.add(card);
//     }

//     // ── Utilities ──────────────────────────────────────────────────────────────

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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

    // ─── Colours ─────────────────────────────────────────────────────────────
    // Header / primary brand = #800080 purple (as requested)
    private static final BaseColor COLOR_PRIMARY      = new BaseColor(128, 0, 128);    // #800080 purple
    private static final BaseColor COLOR_PRIMARY_DARK = new BaseColor(96,  0,  96);    // darker purple for right panel
    private static final BaseColor COLOR_SUCCESS      = new BaseColor(22, 163, 74);    // #16A34A green
    private static final BaseColor COLOR_WARNING      = new BaseColor(202, 138, 4);    // #CA8A04 amber
    private static final BaseColor COLOR_DANGER       = new BaseColor(220, 38,  38);   // #DC2626 red
    private static final BaseColor COLOR_BG_LIGHT     = new BaseColor(252, 252, 255);  // near-white with a hint of purple
    private static final BaseColor COLOR_BORDER       = new BaseColor(229, 231, 235);  // #E5E7EB neutral gray
    private static final BaseColor COLOR_TEXT_DARK    = new BaseColor(17,  24,  39);   // #111827
    private static final BaseColor COLOR_TEXT_MUTED   = new BaseColor(107, 114, 128);  // #6B7280
    private static final BaseColor COLOR_SECTION_BG   = new BaseColor(250, 245, 255);  // very light purple tint
    private static final BaseColor COLOR_BAR_BG       = new BaseColor(229, 231, 235);  // progress-bar track
    private static final BaseColor COLOR_STRENGTH_BG  = new BaseColor(240, 253, 244);  // light green bg
    private static final BaseColor COLOR_DANGER_BG    = new BaseColor(255, 241, 242);  // light red bg

    // ─── Fonts ───────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE      = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   BaseColor.WHITE);
    private static final Font FONT_SUBTITLE   = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(220, 180, 220));
    private static final Font FONT_NAME       = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD,   BaseColor.WHITE);
    private static final Font FONT_LABEL      = new Font(Font.FontFamily.HELVETICA, 8,  Font.BOLD,   COLOR_TEXT_MUTED);
    private static final Font FONT_VALUE      = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXT_DARK);
    private static final Font FONT_SKILL_NAME = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_TEXT_DARK);
    private static final Font FONT_SKILL_LVL  = new Font(Font.FontFamily.HELVETICA, 9,  Font.ITALIC, COLOR_TEXT_MUTED);
    private static final Font FONT_SKILL_PCT  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_PRIMARY);
    private static final Font FONT_BODY       = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, COLOR_TEXT_MUTED);
    private static final Font FONT_BULLET     = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_TEXT_DARK);
    private static final Font FONT_SCORE_NUM  = new Font(Font.FontFamily.HELVETICA, 32, Font.BOLD,   BaseColor.WHITE);
    private static final Font FONT_SCORE_LBL  = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, new BaseColor(220, 180, 220));
    private static final Font FONT_Q          = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_PRIMARY);
    private static final Font FONT_Q_BODY     = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, COLOR_TEXT_DARK);
    private static final Font FONT_LINK       = new Font(Font.FontFamily.HELVETICA, 10, Font.UNDERLINE, COLOR_PRIMARY);

    // =========================================================================
    // PUBLIC API
    // =========================================================================

    // public void saveAnalysis(AnalysisRequestDto request) {
    //     InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
    //             .orElseThrow(() -> new RuntimeException("Invalid token"));

    //     AnalysisEntity analysis = AnalysisEntity.builder()
    //             .interviewLinkId(interviewLink.getId())
    //             .analysis(request.getAnalysis())
    //             .status(1)
    //             .build();

    //     analysisRepository.save(analysis);
    // }


//     @Transactional
// public void saveAnalysis(AnalysisRequestDto request) {
//     InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
//             .orElseThrow(() -> new RuntimeException("Invalid token"));

//     // Find if a record already exists (e.g., created by saveDuration)
//     AnalysisEntity existingAnalysis = analysisRepository
//             .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
//             .orElse(null);

//     if (existingAnalysis != null) {
//         // Update the existing record with the actual AI analysis
//         existingAnalysis.setAnalysis(request.getAnalysis());
//         analysisRepository.save(existingAnalysis);
//     } else {
//         // Create a brand new record
//         AnalysisEntity newAnalysis = AnalysisEntity.builder()
//                 .interviewLinkId(interviewLink.getId())
//                 .analysis(request.getAnalysis())
//                 .status(1)
//                 .build();
//         analysisRepository.save(newAnalysis);
//     }
// }


//     @Transactional
// public void saveDuration(DurationRequestDto request) {
//     // 1. Validate the token and find the Interview Link
//     InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
//             .orElseThrow(() -> new RuntimeException("Invalid token: " + request.getToken()));

//     // 2. Try to find an existing Analysis record for this attempt
//     AnalysisEntity analysis = analysisRepository
//             .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
//             .orElse(null);

//     if (analysis != null) {
       
//         analysis.setDuration(request.getDuration());
//         analysisRepository.save(analysis);
//     } else {
        
//         AnalysisEntity newAnalysis = AnalysisEntity.builder()
//                 .interviewLinkId(interviewLink.getId())
//                 .analysis("") 
//                 .status(1)
//                 .duration(request.getDuration())
//                 .build();
//         analysisRepository.save(newAnalysis);
//     }
// }


@Transactional
public void saveAnalysis(AnalysisRequestDto request) {
    // 1. Validate Token
    InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    // 2. Find existing record (if saveDuration or a previous saveAnalysis ran first)
    AnalysisEntity existingAnalysis = analysisRepository
            .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
            .orElse(null);

    if (existingAnalysis != null) {
        // UPDATE: Just overwrite the analysis text
        existingAnalysis.setAnalysis(request.getAnalysis());
        existingAnalysis.setStatus(1);
        analysisRepository.save(existingAnalysis);
    } else {
        // INSERT: Create a brand new record
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
    // 1. Validate Token
    InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    // 2. Find existing record (if saveAnalysis ran first)
    AnalysisEntity existingAnalysis = analysisRepository
            .findTopByInterviewLinkIdOrderByCreatedAtDesc(interviewLink.getId())
            .orElse(null);

    if (existingAnalysis != null) {
        // UPDATE: Just add the duration
        existingAnalysis.setDuration(request.getDuration());
        analysisRepository.save(existingAnalysis);
    } else {
        // INSERT: Create a stub record with empty JSON to satisfy the non-null constraint
        AnalysisEntity newAnalysis = AnalysisEntity.builder()
                .interviewLinkId(interviewLink.getId())
                .analysis("{}") // Empty JSON placeholder
                .duration(request.getDuration())
                .status(1)
                .build();
        analysisRepository.save(newAnalysis);
    }
}

 

    /** Minimal overload — no candidate meta. */
    public String generateAnalysisPdf(Long interviewId, String analysisJson) {
        return generateAnalysisPdf(interviewId, analysisJson,
                null, null, null, null, null, null, null, null);
    }

    /**
     * Full overload — call from InterviewService:
     *
     *   analysisService.generateAnalysisPdf(
     *       link.getId(),
     *       analysis.get().getAnalysis(),
     *       interview.getCandidateName(),
     *       interview.getEmail(),
     *       interview.getPhoneNumber(),
     *       link.getInterviewLink(),
     *       interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null,
     *       interview.getInterviewDuration(),
     *       interview.getVideoLink(),
     *       interview.getTranscriptionLink()
     *   );
     */
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

            if (file.exists()) {
                return "/uploads/analysis/" + fileName;
            }

            // ── Parse JSON ───────────────────────────────────────────────────
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
                    if (closeCount > openCount) {
                        fragment = fragment.substring(0, fragment.lastIndexOf('}'));
                    }
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

            // ── Build PDF ────────────────────────────────────────────────────
            Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            // 1. Header banner
            addHeaderBanner(doc, candidateName, candidateEmail, candidatePhone,
                    analysis.path("overall_score").asText("N/A"));

            addSpacer(doc);

            // 2. Candidate info card (vertical list)
            addCandidateInfoCard(doc, candidateName, candidateEmail, candidatePhone,
                    interviewDate, interviewDuration, interviewLink, videoLink, transcriptionLink);

            addSpacer(doc);

            // 3. Category scores
            JsonNode catScores = analysis.path("category_scores");
            if (!catScores.isMissingNode() && !catScores.isEmpty()) {
                addCategoryScores(doc, catScores);
                addSpacer(doc);
            }

            // 4. Must Have Skills
            addSectionHeader(doc, "  Must Have Skills", COLOR_SUCCESS);
            JsonNode mustSkills = analysis.path("must_to_have_skills");
            if (mustSkills.isArray()) {
                for (JsonNode skill : mustSkills) addSkillRow(doc, skill, COLOR_SUCCESS);
            }
            addSpacer(doc);

            // 5. Good to Have Skills
            addSectionHeader(doc, "  Good to Have Skills", COLOR_WARNING);
            JsonNode goodSkills = analysis.path("good_to_have_skills");
            if (goodSkills.isArray()) {
                for (JsonNode skill : goodSkills) addSkillRow(doc, skill, COLOR_WARNING);
            }
            addSpacer(doc);

            // 6. Strengths (bullet list with description — same style as improvements)
            JsonNode strengths = analysis.path("top_strengths");
            if (strengths.isArray() && strengths.size() > 0) {
                addSectionHeader(doc, "  Top Strengths", COLOR_SUCCESS);
                for (JsonNode s : strengths) {
                    // top_strengths may be plain strings or objects {area, description}
                    String heading = s.isTextual()
                            ? s.asText("")
                            : s.path("area").asText(s.path("strength").asText(""));
                    String desc = s.isTextual() ? "" : s.path("description").asText("");
                    addBulletItem(doc, heading, desc, COLOR_SUCCESS, COLOR_STRENGTH_BG);
                }
                addSpacer(doc);
            }

            // 7. Weaknesses / Areas for Improvement
            JsonNode improvements = analysis.path("areas_for_improvement");
            if (improvements.isArray() && improvements.size() > 0) {
                addSectionHeader(doc, "  Areas for Improvement/Weakness", COLOR_DANGER);
                for (JsonNode area : improvements) {
                    addBulletItem(doc,
                            area.path("area").asText(""),
                            area.path("description").asText(""),
                            COLOR_DANGER, COLOR_DANGER_BG);
                }
                addSpacer(doc);
            }

            // 8. Interview Q&A
            JsonNode questions = analysis.path("interview_questions_responses");
            if (questions.isArray() && questions.size() > 0) {
                addSectionHeader(doc, "  Interview Questions & Responses", COLOR_PRIMARY);
                for (JsonNode q : questions) addQuestionBlock(doc, q);
                addSpacer(doc);
            }

            // 9. Overall Summary
            addSectionHeader(doc, "  Overall Interview Summary", COLOR_PRIMARY);
            PdfPTable summaryBox = new PdfPTable(1);
            summaryBox.setWidthPercentage(100);
            PdfPCell summaryCell = new PdfPCell();
            summaryCell.setBackgroundColor(COLOR_SECTION_BG);
            summaryCell.setBorderColor(COLOR_PRIMARY);
            summaryCell.setBorderWidth(0.5f);
            summaryCell.setBorderWidthLeft(4f);
            summaryCell.setPaddingTop(10);
            summaryCell.setPaddingBottom(10);
            summaryCell.setPaddingLeft(14);
            summaryCell.setPaddingRight(14);
            Paragraph summaryPara = new Paragraph(
                    analysis.path("overall_ai_summary").asText("N/A"), FONT_Q_BODY);
            summaryPara.setLeading(14f);
            summaryCell.addElement(summaryPara);
            summaryBox.addCell(summaryCell);
            doc.add(summaryBox);

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
        banner.setWidths(new float[]{3.2f, 0.9f});

        // Left cell — purple brand
        PdfPCell left = new PdfPCell();
        left.setBackgroundColor(COLOR_PRIMARY);  // #800080
        left.setBorder(Rectangle.NO_BORDER);
        left.setPaddingLeft(20);
        left.setPaddingTop(18);
        left.setPaddingBottom(18);
        left.setPaddingRight(12);
        left.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph titlePara = new Paragraph("INTERVIEW ANALYSIS REPORT", FONT_TITLE);
        left.addElement(titlePara);

        String displayName = (name != null && !name.isEmpty()) ? name : "Candidate";
        Paragraph namePara = new Paragraph(displayName, FONT_NAME);
        namePara.setSpacingBefore(6);
        left.addElement(namePara);

        if (email != null && !email.isEmpty()) {
            String contact = email + (phone != null && !phone.isEmpty() ? "   |   " + phone : "");
            Paragraph contactPara = new Paragraph(contact, FONT_SUBTITLE);
            contactPara.setSpacingBefore(3);
            left.addElement(contactPara);
        }
        banner.addCell(left);

        // Right cell — darker purple, score
        PdfPCell right = new PdfPCell();
        right.setBackgroundColor(COLOR_PRIMARY_DARK);
        right.setBorder(Rectangle.NO_BORDER);
        right.setPadding(16);
        right.setHorizontalAlignment(Element.ALIGN_CENTER);
        right.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph scoreNum = new Paragraph(score + "/100", FONT_SCORE_NUM);
        scoreNum.setAlignment(Element.ALIGN_CENTER);
        right.addElement(scoreNum);

        Paragraph scoreLbl = new Paragraph("Overall Score", FONT_SCORE_LBL);
        scoreLbl.setAlignment(Element.ALIGN_CENTER);
        scoreLbl.setSpacingBefore(2);
        right.addElement(scoreLbl);

        banner.addCell(right);
        doc.add(banner);
    }

    // =========================================================================
    // SECTION: CANDIDATE INFO CARD  (vertical label: value list)
    // =========================================================================

    /**
     * Renders candidate details as a clean vertical list — one field per line.
     * Links (Interview, Video, Transcription) are clickable blue underlined text.
     *
     * PARAMETER ORDER (must match the call in generateAnalysisPdf):
     *   name, email, phone, date, duration, interviewLink, videoLink, transcriptionLink
     */
    private void addCandidateInfoCard(Document doc,
                                       String name,
                                       String email,
                                       String phone,
                                       String interviewDate,
                                       String interviewDuration,
                                       String interviewLink,
                                       String videoLink,
                                       String transcriptionLink) throws Exception {

        PdfPTable wrapper = new PdfPTable(1);
        wrapper.setWidthPercentage(100);
        wrapper.setSpacingBefore(4);

        PdfPCell card = new PdfPCell();
        card.setBackgroundColor(COLOR_BG_LIGHT);
        card.setBorderColor(COLOR_BORDER);
        card.setBorderWidth(0.5f);
        card.setBorderWidthLeft(4f);
        card.setBorderColorLeft(COLOR_PRIMARY);
        card.setPaddingTop(12);
        card.setPaddingBottom(14);
        card.setPaddingLeft(16);
        card.setPaddingRight(16);

        // Plain text rows
        card.addElement(makeLabelValueRow("Candidate Name",    name));
        card.addElement(makeLabelValueRow("Email ID",          email));
        card.addElement(makeLabelValueRow("Phone",             phone));
        card.addElement(makeLabelValueRow("Interview Date",    interviewDate));
        card.addElement(makeLabelValueRow("Interview Duration",interviewDuration));

        // Link rows  ← order is important, must match parameter order above
        card.addElement(makeLinkRow("Interview Link",    interviewLink));
        card.addElement(makeLinkRow("Video Link",        videoLink));
        card.addElement(makeLinkRow("Transcription Link",transcriptionLink));

        wrapper.addCell(card);
        doc.add(wrapper);
    }

    /** Plain text:  LABEL:   value */
    private Paragraph makeLabelValueRow(String label, String value) {
        String display = (value != null && !value.isBlank()) ? value : "\u2014";
        Paragraph p = new Paragraph();
        p.setSpacingBefore(5);
        p.add(new Chunk(label + ":   ", FONT_LABEL));
        p.add(new Chunk(display, FONT_VALUE));
        return p;
    }

    /** Hyperlink row:  LABEL:   <clickable url> */
    private Paragraph makeLinkRow(String label, String url) {
        Paragraph p = new Paragraph();
        p.setSpacingBefore(5);
        p.add(new Chunk(label + ":   ", FONT_LABEL));

        if (url != null && !url.isBlank()) {
            Chunk link = new Chunk(url, FONT_LINK);
            link.setAction(new PdfAction(url));   // real clickable hyperlink
            p.add(link);
        } else {
            p.add(new Chunk("\u2014", FONT_VALUE));
        }
        return p;
    }

    // =========================================================================
    // SECTION: CATEGORY SCORES
    // =========================================================================

    private void addCategoryScores(Document doc, JsonNode catScores) throws Exception {
        addSectionHeader(doc, "  Category Scores", COLOR_PRIMARY);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(catScores).trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);

        List<String>  fieldNames = new ArrayList<>();
        List<Integer> pctValues  = new ArrayList<>();

        for (String pair : json.split(",")) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim().replace("\"", "");
                String val = kv[1].trim().replace("\"", "");
                fieldNames.add(key);
                try { pctValues.add(Integer.parseInt(val)); }
                catch (NumberFormatException e) { pctValues.add(0); }
            }
        }

        if (fieldNames.isEmpty()) return;

        PdfPTable table = new PdfPTable(fieldNames.size());
        table.setWidthPercentage(100);
        table.setSpacingBefore(4);

        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            int    pct = pctValues.get(i);

            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(COLOR_SECTION_BG);
            cell.setBorderColor(COLOR_BORDER);
            cell.setBorderWidth(0.5f);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Paragraph scoreP = new Paragraph(String.valueOf(pct),
                    new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, scoreColor(pct)));
            scoreP.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(scoreP);

            Paragraph barHolder = new Paragraph();
            barHolder.setSpacingBefore(4);
            cell.addElement(barHolder);
            cell.addElement(buildProgressBar(pct));

            Paragraph labelP = new Paragraph(toTitleCase(key.replace("_", " ")), FONT_LABEL);
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

    private void addSectionHeader(Document doc, String text, BaseColor color) throws Exception {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        header.setSpacingBefore(12);
        header.setSpacingAfter(6);

        PdfPCell cell = new PdfPCell(
                new Phrase(text, new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, color)));
        cell.setBackgroundColor(COLOR_SECTION_BG);
        cell.setBorderWidthLeft(4f);
        cell.setBorderColorLeft(color);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColorBottom(COLOR_BORDER);
        cell.setPaddingTop(8);
        cell.setPaddingBottom(8);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(8);
        header.addCell(cell);

        doc.add(header);
    }

    // =========================================================================
    // SECTION: SKILL ROW  (progress bar card)
    // =========================================================================

    private void addSkillRow(Document doc, JsonNode skill, BaseColor accentColor) throws Exception {
        String skillName = skill.path("skill_name").asText("N/A");
        String desc      = skill.path("description").asText("");
        int    pct       = skill.path("percentage").asInt(0);
        String level     = getLevel(pct);

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(4);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
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
        topRow.setWidths(new float[]{4f, 1.4f, 0.9f});
        topRow.setWidthPercentage(100);

        PdfPCell nameCell = new PdfPCell(new Phrase(skillName, FONT_SKILL_NAME));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(nameCell);

        PdfPCell lvlCell = new PdfPCell(new Phrase(level, FONT_SKILL_LVL));
        lvlCell.setBorder(Rectangle.NO_BORDER);
        lvlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        lvlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(lvlCell);

        PdfPCell pctCell = new PdfPCell(new Phrase(pct + "/100", FONT_SKILL_PCT));
        pctCell.setBorder(Rectangle.NO_BORDER);
        pctCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pctCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        topRow.addCell(pctCell);

        cell.addElement(topRow);
        cell.addElement(buildProgressBar(pct));

        if (!desc.isEmpty()) {
            Paragraph descPara = new Paragraph(desc, FONT_BODY);
            descPara.setSpacingBefore(5);
            descPara.setLeading(13f);
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
            c.setFixedHeight(5f);
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
            c.setFixedHeight(5f);
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
        filledCell.setFixedHeight(5f);
        filledCell.setBackgroundColor(scoreColor(pct));
        filledCell.setBorder(Rectangle.NO_BORDER);
        bar.addCell(filledCell);

        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setFixedHeight(5f);
        emptyCell.setBackgroundColor(COLOR_BAR_BG);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        bar.addCell(emptyCell);

        return bar;
    }

    // =========================================================================
    // SECTION: BULLET ITEM  (Strengths & Improvements — identical style)
    // =========================================================================

    /**
     * Renders one bullet card with a coloured left border, heading in bold,
     * and an optional description in muted text — same layout for both
     * Top Strengths and Areas for Improvement.
     */
    private void addBulletItem(Document doc, String heading, String body,
                                BaseColor borderColor, BaseColor bgColor) throws Exception {
        if (heading == null || heading.isBlank()) return;

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(4);

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(borderColor);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColorBottom(COLOR_BORDER);
        cell.setBackgroundColor(bgColor);
        cell.setPaddingTop(8);
        cell.setPaddingBottom(8);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(12);

        Paragraph headPara = new Paragraph("\u2022  " + heading, FONT_BULLET);
        cell.addElement(headPara);

        if (body != null && !body.isBlank()) {
            Paragraph bodyPara = new Paragraph(body, FONT_BODY);
            bodyPara.setIndentationLeft(14);
            bodyPara.setSpacingBefore(3);
            bodyPara.setLeading(13f);
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

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.setSpacingBefore(6);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(0.5f);
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(COLOR_PRIMARY);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(12);
        cell.setPaddingRight(12);

        // Q + score on same line
        PdfPTable qRow = new PdfPTable(2);
        qRow.setWidths(new float[]{5f, 1f});
        qRow.setWidthPercentage(100);

        PdfPCell qCell = new PdfPCell(new Phrase("Q: " + question, FONT_Q));
        qCell.setBorder(Rectangle.NO_BORDER);
        qRow.addCell(qCell);

        BaseColor scoreCol = score >= 60 ? COLOR_SUCCESS
                           : score >= 30 ? COLOR_WARNING
                                         : COLOR_DANGER;
        PdfPCell scoreCell = new PdfPCell(new Phrase("Score: " + score + "/100",
                new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, scoreCol)));
        scoreCell.setBorder(Rectangle.NO_BORDER);
        scoreCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        scoreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        qRow.addCell(scoreCell);

        cell.addElement(qRow);
        cell.addElement(buildProgressBar(score));

        if (!response.isEmpty()) {
            Paragraph resp = new Paragraph("Response Summary:  " + response, FONT_Q_BODY);
            resp.setSpacingBefore(7);
            resp.setLeading(13f);
            cell.addElement(resp);
        }

        JsonNode insights = q.path("key_insights");
        if (insights.isArray() && insights.size() > 0) {
            Paragraph insightsTitle = new Paragraph("Key Insights:",
                    new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_TEXT_MUTED));
            insightsTitle.setSpacingBefore(7);
            cell.addElement(insightsTitle);
            for (JsonNode insight : insights) {
                Paragraph ins = new Paragraph("  \u2022  " + insight.asText(), FONT_BODY);
                ins.setSpacingBefore(2);
                cell.addElement(ins);
            }
        }

        JsonNode missed = q.path("missed_opportunities");
        if (missed.isArray() && missed.size() > 0) {
            Paragraph missedTitle = new Paragraph("Missed Opportunities:",
                    new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_DANGER));
            missedTitle.setSpacingBefore(6);
            cell.addElement(missedTitle);
            for (JsonNode m : missed) {
                Paragraph mp = new Paragraph("  \u2022  " + m.asText(), FONT_BODY);
                mp.setSpacingBefore(2);
                cell.addElement(mp);
            }
        }

        card.addCell(cell);
        doc.add(card);
    }

    // =========================================================================
    // UTILITIES
    // =========================================================================

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
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}


