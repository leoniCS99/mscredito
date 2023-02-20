package avaliador.mscredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DadosCliente {
    private Long id;
    private String nome;
    private Integer idade;
}
