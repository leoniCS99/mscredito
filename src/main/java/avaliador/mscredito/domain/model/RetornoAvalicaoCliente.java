package avaliador.mscredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RetornoAvalicaoCliente {
    private List<CartaoAprovado> cartoes;
}
