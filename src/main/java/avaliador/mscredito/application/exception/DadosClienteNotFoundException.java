package avaliador.mscredito.application.exception;

public class DadosClienteNotFoundException extends Exception{

    public DadosClienteNotFoundException() {
        super("Dados não encontrado para CPF informado");
    }
}
