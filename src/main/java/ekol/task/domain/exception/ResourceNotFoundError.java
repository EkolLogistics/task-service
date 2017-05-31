package ekol.task.domain.exception;

import java.io.Serializable;
import java.text.MessageFormat;

public class ResourceNotFoundError extends RuntimeException {
    private transient Object[] args = new Serializable[]{};

    public ResourceNotFoundError(String message, Object... args) {
        super(message);
        this.args = args;
    }
    @Override
    public String getMessage(){
        if(this.args.length == 0){
            return super.getMessage();
        }
        MessageFormat messageFormat = new MessageFormat(super.getMessage());
        return messageFormat.format(this.args);
    }
}
