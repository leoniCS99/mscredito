package avaliador.mscredito.application;

import avaliador.mscredito.application.exception.DadosClienteNotFoundException;
import avaliador.mscredito.application.exception.ErroComunicaoMicroservicesException;
import avaliador.mscredito.application.exception.ErroSolicitacaoCartaoException;
import avaliador.mscredito.domain.model.*;
import avaliador.mscredito.infra.clients.CartoesResourceClient;
import avaliador.mscredito.infra.clients.ClienteResourceClient;
import avaliador.mscredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;

    private final CartoesResourceClient cartoesClient;

    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException,
            ErroComunicaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClientesResponse = clientesClient.dadosClientes(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);


            return SituacaoCliente
                    .builder()
                    .cliente(dadosClientesResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicaoMicroservicesException(e.getMessage(), status);
        }

    }

    public RetornoAvalicaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClientesResponse = clientesClient.dadosClientes(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.cartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovado = cartoes.stream().map(cartao -> {


                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idade = BigDecimal.valueOf(dadosClientesResponse.getBody().getIdade());
                var fator = idade.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvalicaoCliente(listaCartoesAprovado);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);

        }catch (Exception e){
        throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}