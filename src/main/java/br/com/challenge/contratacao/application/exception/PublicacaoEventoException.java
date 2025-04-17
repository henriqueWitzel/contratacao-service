package br.com.challenge.contratacao.application.exception;

/**
 * Exceção lançada quando ocorre falha na publicação de eventos.
 */
public class PublicacaoEventoException extends RuntimeException {

    public PublicacaoEventoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}