package xadrez;

import tabuleiro.Posicao;

public class XadrezPosicao {

    private char coluna;;
    private int linha;
    
    public XadrezPosicao(char coluna, int linha) {
        if(coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8){
            throw new XadrezException("Erro ao instanciar a posicao. Valores validos são de A1 ate H8");
        }
        this.coluna = coluna;
        this.linha = linha;
    }

    public char getColuna() {
        return coluna;
    }

    
    public int getLinha() {
        return linha;
    }

    protected Posicao toPosicao(){
        return new Posicao(8 - linha, coluna - 'a');
    }

    protected static XadrezPosicao fromPosicao(Posicao posicao){
        return new XadrezPosicao((char)('a' + posicao.getColuna()), 8 - posicao.getLinha());
    }

    @Override   
    public String toString(){
        return "" + coluna + linha;
    }

    

    

}
