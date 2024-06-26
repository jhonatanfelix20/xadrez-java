package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

    private int turno;
    private Color jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean check;
    private boolean checkMate;
    private PecaXadrez enPassantVuneravel;
    private PecaXadrez promocao;

    private List<Peca> pecasDoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Color.WHITE;
        check = false;
        configuracaoInicial();
    }

    public int getTurno() {
        return turno;
    }

    public Color getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public PecaXadrez getEnPassantVuneral() {
        return enPassantVuneravel;
    }

    public PecaXadrez getPromocao() {
        return promocao;
    }

    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(XadrezPosicao origemPosicao) {
        Posicao posicao = origemPosicao.toPosicao();
        validarPosicaoDeOrigem(posicao);
        return tabuleiro.peca(posicao).movimentosPossiveis();
    }

    public PecaXadrez MovimentoXadrez(XadrezPosicao origemPosicao, XadrezPosicao destinoPosicao) {
        Posicao origem = origemPosicao.toPosicao();
        Posicao destino = destinoPosicao.toPosicao();
        validarPosicaoDeOrigem(origem);
        validarPosicaoDeDestino(origem, destino);
        Peca capturaPeca = mover(origem, destino);
        if (testCheck(jogadorAtual)) {
            desfazerMovimento(origem, destino, capturaPeca);
            throw new XadrezException("Voce nao pode se colocar em check");
        }

        PecaXadrez pecaMoveu = (PecaXadrez) tabuleiro.peca(destino);

        // #specialmove promotion
        promocao = null;
        if (pecaMoveu instanceof Peao) {
            if ((pecaMoveu.getCor() == Color.WHITE && destino.getLinha() == 0)
                    || (pecaMoveu.getCor() == Color.BLACK && destino.getLinha() == 7)) {
                promocao = (PecaXadrez) tabuleiro.peca(destino);
                promocao = substituirPeca("Q");
            }
        }

        check = (testCheck(oponente(jogadorAtual))) ? true : false;

        if (testCheckMate(oponente(jogadorAtual))) {
            checkMate = true;
        } else {
            proximoTurno();
        }

        // #specialmove en passant
        if (pecaMoveu instanceof Peao
                && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            enPassantVuneravel = pecaMoveu;
        } else {
            enPassantVuneravel = null;
        }

        return (PecaXadrez) capturaPeca;
    }

    public PecaXadrez substituirPeca(String type) {
        if (promocao == null) {
            throw new XadrezException("Nao ha peca para ser promovida");
        }
        if (!type.equals("B") && !type.equals("C") && !type.equals("R") && !type.equals("Q")) {
            return promocao;
        }

        Posicao pos = promocao.getXadrezPosicao().toPosicao();
        Peca p = tabuleiro.removaPeca(pos);
        pecasDoTabuleiro.remove(p);

        PecaXadrez novaPeca = novaPeca(type, promocao.getCor());
        tabuleiro.colocarPeca(novaPeca, pos);
        pecasDoTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private PecaXadrez novaPeca(String type, Color cor) {
        if (type.equals("B"))
            return new Bispo(tabuleiro, cor);
        if (type.equals("C"))
            return new Cavalo(tabuleiro, cor);
        if (type.equals("Q"))
            return new Rainha(tabuleiro, cor);
        return new Torre(tabuleiro, cor);
    }

    private Peca mover(Posicao origem, Posicao destino) {
        PecaXadrez p = (PecaXadrez) tabuleiro.removaPeca(origem);
        p.incrementarContadorMovimentos();
        Peca capturaPeca = tabuleiro.removaPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecasCapturadas != null) {
            pecasDoTabuleiro.remove(capturaPeca);
            pecasCapturadas.add(capturaPeca);
        }

        // #specialmove roque torre do lado do rei
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removaPeca(origemTorre);
            tabuleiro.colocarPeca(torre, destinoTorre);
            torre.incrementarContadorMovimentos();
        }

        // #specialmove roque torre do lado da rainha
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removaPeca(origemTorre);
            tabuleiro.colocarPeca(torre, destinoTorre);
            torre.incrementarContadorMovimentos();
        }

        // #specialmove en passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && capturaPeca == null) {
                Posicao peaoPosicao;
                if (p.getCor() == Color.WHITE) {
                    peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                capturaPeca = tabuleiro.removaPeca(peaoPosicao);
                pecasCapturadas.add(capturaPeca);
                pecasDoTabuleiro.remove(capturaPeca);
            }
        }

        return capturaPeca;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturaPeca) {
        PecaXadrez p = (PecaXadrez) tabuleiro.removaPeca(destino);
        p.decrementarContadorMovimentos();
        tabuleiro.colocarPeca(p, origem);
        if (capturaPeca != null) {
            tabuleiro.colocarPeca(capturaPeca, destino);
            pecasCapturadas.remove(capturaPeca);
            pecasDoTabuleiro.add(capturaPeca);
        }

        // #specialmove roque torre do lado do rei
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removaPeca(destinoTorre);
            tabuleiro.colocarPeca(torre, origemTorre);
            torre.decrementarContadorMovimentos();
        }

        // #specialmove roque torre do lado da rainha
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removaPeca(destinoTorre);
            tabuleiro.colocarPeca(torre, origemTorre);
            torre.decrementarContadorMovimentos();
        }
        // #specialmove en passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && capturaPeca == enPassantVuneravel) {
                PecaXadrez peao = (PecaXadrez) tabuleiro.removaPeca(destino);
                Posicao peaoPosicao;
                if (p.getCor() == Color.WHITE) {
                    peaoPosicao = new Posicao(3, destino.getColuna());
                } else {
                    peaoPosicao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, peaoPosicao);
            }
        }
    }

    private void validarPosicaoDeOrigem(Posicao posicao) {
        if (!tabuleiro.existePeca(posicao)) {
            throw new XadrezException("Não existe peca na posicao de origem");
        }
        if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
            throw new XadrezException("A peca escolhida não é sua");
        }
        if (!tabuleiro.peca(posicao).existeMovimentosPossiveis()) {
            throw new XadrezException("Não existe movimentos possíveis para a peca escolhida");
        }
    }

    private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
            throw new XadrezException("A peça escolhida não pode se mover para posição de destino");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color oponente(Color cor) {
        return (cor == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private PecaXadrez Rei(Color cor) {
        List<Peca> list = pecasDoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("Não existe rei da cor " + cor + " no tabuleiro");
    }

    private boolean testCheck(Color cor) {
        Posicao posicaoRei = Rei(cor).getXadrezPosicao().toPosicao();
        List<Peca> pecasOponente = pecasDoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
                .collect(Collectors.toList());
        for (Peca p : pecasOponente) {
            boolean[][] mat = p.movimentosPossiveis();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    public boolean testCheckMate(Color cor) {
        if (!testCheck(cor)) {
            return false;
        }
        List<Peca> list = pecasDoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());
        for (Peca p : list) {
            boolean[][] mat = p.movimentosPossiveis();
            for (int i = 0; i < tabuleiro.getLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = ((PecaXadrez) p).getXadrezPosicao().toPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca capturaPeca = mover(origem, destino);
                        boolean testCheck = testCheck(cor);
                        desfazerMovimento(origem, destino, capturaPeca);
                        if (!testCheck) {
                            return false;
                        }
                    }

                }
            }
        }
        return true;

    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
        pecasDoTabuleiro.add(peca);
    }

    private void configuracaoInicial() {
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Color.WHITE));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Color.WHITE));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Color.WHITE));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Color.WHITE, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Color.WHITE, this));

        colocarNovaPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Color.BLACK));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Color.BLACK, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Color.BLACK, this));
    }

}
