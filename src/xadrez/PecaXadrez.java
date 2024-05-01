package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca{

    private Color cor;

    public PecaXadrez(Tabuleiro tabuleiro, Color cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Color getCor() {
        return cor;
    }

    protected boolean existePecaOponente(Posicao posicao){
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
    

}
