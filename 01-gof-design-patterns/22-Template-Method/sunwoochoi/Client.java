/**
 * Client
 */
public class Client {
  public static void main(String[] args) {
    AbstractDataParser chatParser = new ChatDataParser("data/chat/chat_1.chat");
    chatParser.templateMethod();
    // Start data parser
    // Load Chat data from data/chat/chat_1.chat
    // Transform raw text data to String: text:Hello, wanna hang out?
    // Abstract-Tokenize string Hello, wanna hang out?
    // Abstract-Removing stop words in place [Hello,, wanna, hang, out?]
    // Abstract-Stemming words in place [Hello,, wanna, hang, out?]
    // Chat-Delete Emoji
    // Abstract-Transform string to vector of [Hello,, wanna, hang, out?]
    // Abstract-Feed data [Hello,, wanna, hang, out?] to model
    // End data parser
    
    AbstractDataParser audioParser = new AudioDataParser("data/audio/audio_1.acc");
    audioParser.templateMethod();
    // Start data parser
    // Load Audio data from data/audio/audio_1.acc
    // Transform raw audio data to String: aac:hello, want to hang out?
    // Abstract-Tokenize string hello, want to hang out?
    // Abstract-Removing stop words in place [hello,, want, to, hang, out?]
    // Abstract-Stemming words in place [hello,, want, to, hang, out?]
    // Abstract-Transform string to vector of [hello,, want, to, hang, out?]
    // Abstract-Feed data [hello,, want, to, hang, out?] to model
    // End data parser
  }
  
}