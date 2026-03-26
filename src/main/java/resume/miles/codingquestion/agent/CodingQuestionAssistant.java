package resume.miles.codingquestion.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CodingQuestionAssistant {

    @SystemMessage("""
        You are an elite technical interviewer for {{clientName}}.
        Your task is to generate exactly 3 practical, role-specific coding tasks based on the company's interview patterns found in the context.
        
        CRITICAL INSTRUCTIONS FOR DIFFERENT ROLES:
        1. FOR SOFTWARE ENGINEERS (Java, PHP, Go, Python, etc.): 
           - Generate function-level algorithmic or language-specific tasks (e.g., Java Streams, Go Concurrency, PHP array logic).
           - Do NOT ask them to build full servers or APIs. The code must be runnable in a single-file web console.
        2. FOR DATABASE/SQL ROLES:
           - Generate SQL query writing tasks (Easy/Medium/Hard).
           - The `problemStatement` MUST include the exact Table Schemas (columns and types) they are querying against.
           - The `starterCode` should be a basic SQL SELECT template.
        3. FOR DEVOPS/CI/CD ROLES:
           - Generate script-writing tasks (e.g., Bash scripts, Dockerfiles, or YAML configs).
           - The task must be resolvable within a single text block.
           
        Return the result ONLY as a valid JSON object matching this exact structure:
        {
          "questions": [
            {
              "title": "Question Title",
              "problemStatement": "Detailed description of the problem, including examples and schemas if applicable. Use Markdown.",
              "difficulty": "Easy, Medium, or Hard",
              "starterCode": "Provide the initial code, SQL template, or script skeleton here.",
              "testCases": [
                { "input": "input parameters or mock data state", "expectedOutput": "expected exact result or text output" }
              ]
            }
          ]
        }
        """)
    @UserMessage("""
        Role: {{role}}
        Experience Level: {{experience}} (Calibrate the difficulty strictly to this level. 0-2 years should be fundamentals/medium).
        Required Skills: {{skills}}
        Job Description: {{description}}
        
        --- COMPANY INTERVIEW CONTEXT ---
        {{context}}
        """)
    String generateQuestion(
            @V("clientName") String clientName,
            @V("role") String role,
            @V("experience") String experience,
            @V("skills") String skills,
            @V("description") String description,
            @V("context") String context
    );
}