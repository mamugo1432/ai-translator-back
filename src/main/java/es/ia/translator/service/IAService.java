package es.ia.translator.service;

import es.ia.translator.model.dto.TranslateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class IAService {

    private final ChatClient chatClient;
    private final ChatClient chatClientWithouthMemory;
    private final ChatMemory chatMemory;

    public IAService(ChatModel chatModel){

        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.chatClientWithouthMemory = ChatClient.builder(chatModel).build();
    }

    public String tranlatePhrase(TranslateRequest request){

        String response = this.chatClientWithouthMemory.prompt()
                .user("Translate from " + request.sourceLanguage() + "(ISO639-1) to " + request.targetLanguage() + "(ISO639-1) this phrase " + request.phrase() + ". Return only the result, if there are two options, choose the most common.")
                .call()
                .content();

        return response;
    }
}
