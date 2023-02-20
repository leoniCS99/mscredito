package avaliador.mscredito.application.exception;

import lombok.Getter;

public class ErroComunicaoMicroservicesException extends Exception {
    @Getter
    private Integer status;

    public ErroComunicaoMicroservicesException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
