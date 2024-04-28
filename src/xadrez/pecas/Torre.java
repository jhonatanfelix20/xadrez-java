package xadrez.pecas;

import tabuleiro.Tabuleiro;
import xadrez.Color;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez{

    public Torre(Tabuleiro tabuleiro, Color cor){
        super(tabuleiro, cor);
    }

    @Override
    public String toString(){
        return "T";
    }

}
