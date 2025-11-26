import {onCall} from "firebase-functions/v2/https";
import {defineSecret} from "firebase-functions/params";
import OpenAI from "openai";

const openaiKey = defineSecret("OPENAI_API_KEY");

export const chatWithOpenAI = onCall(
  {secrets: [openaiKey]},
  async (request) => {
    try {
      const openai = new OpenAI({
        apiKey: openaiKey.value(),
      });

      const {messages} = request.data;

      const response = await openai.chat.completions.create({
        model: "gpt-3.5-turbo",
        messages: messages,
        max_tokens: 1000,
        temperature: 0.7,
      });

      return {
        success: true,
        message: response.choices[0].message.content,
        isComplete: response.choices[0].finish_reason === "stop"
      };
    } catch (error: unknown) {
      console.error("OpenAI API Error:", error);
      const message = error instanceof Error ? error.message : "Failed to get response";
      throw new Error(message);
    }
  }
);