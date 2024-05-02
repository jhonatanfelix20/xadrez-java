package xadrez.pecas;

import xadrez.PecaXadrez;
import xadrez.Color;
import xadrez.PartidaXadrez;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class Rei extends PecaXadrez{

    private PartidaXadrez partidaXadrez;

    public Rei(Tabuleiro tabuleiro, Color cor, PartidaXadrez partidaXadrez) {
        super(tabuleiro, cor);
        this.partidaXadrez = partidaXadrez;
    }

    @Override
    public String toString(){
        return "R";
    }

    private boolean podeMover(Posicao posicao){
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    private boolean testeTorreRoque(Posicao posicao){
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContadorMovimentos() == 0;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        //acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //abaixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //noroeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }


        //nordeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //sudoeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //sudeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if(getTabuleiro().posicaoExiste(p) && podeMover(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // #specialmove Roque
        if(getContadorMovimentos() == 0 && !partidaXadrez.getCheck()){

            // #specialmove Roque pequeno
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (testeTorreRoque(posT1)){
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null){
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }

            // #specialmove Roque grande)
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (testeTorreRoque(posT2)){
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null){
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }


        }

        return mat;
    }

}
