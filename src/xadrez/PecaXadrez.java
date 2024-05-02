package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca{

    private Color cor;
    private int contadorMovimentos;

    public PecaXadrez(Tabuleiro tabuleiro, Color cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Color getCor() {
        return cor;
    }

    public int getContadorMovimentos() {
        return contadorMovimentos;
    }

    public void incrementarContadorMovimentos(){
        contadorMovimentos++;
    } 

    public void decrementarContadorMovimentos(){
        contadorMovimentos--;
    }

    public XadrezPosicao getXadrezPosicao(){
        return XadrezPosicao.fromPosicao(posicao);
    }

    protected boolean existePecaOponente(Posicao posicao){
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
    

}
