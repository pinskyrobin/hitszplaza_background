package exception;

public class WeChatException extends RuntimeException{
    public WeChatException(){}
    public WeChatException(String msg) {
        super(msg);
    }
}
