package xadrez;

import tabuleiro.Peca;
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
    

}
