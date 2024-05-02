package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.XadrezException;
import xadrez.XadrezPosicao;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        List<PecaXadrez> capturadas = new ArrayList<>();

        while (!partidaXadrez.getCheckMate()) {
            try{
                UI.clearScreen();
                UI.printPartida(partidaXadrez, capturadas);
                System.out.println();
                System.out.print("Origem: ");
                XadrezPosicao  origem = UI.lerPosicaoXadrez(sc);

                boolean[][] movimentosPossiveis = partidaXadrez.movimentosPossiveis(origem);
                UI.clearScreen();
                UI.printTabuleiro(partidaXadrez.getPecas(), movimentosPossiveis);


                System.out.println();
                System.out.print("Destino: ");
                XadrezPosicao  destino = UI.lerPosicaoXadrez(sc);

                PecaXadrez capturaPeca = partidaXadrez.MovimentoXadrez(origem, destino);

                if (capturaPeca != null) {
                    capturadas.add(capturaPeca);
                    
                }
                if (partidaXadrez.getPromocao() != null) {
                    System.out.print("Entre com a peca para promocao (B/C/R/Q): ");
                    String type = sc.nextLine().toUpperCase();
                    while(!type.equals("B") && !type.equals("C") && !type.equals("R") && !type.equals("Q")) {
                        System.out.print("Valor errado! Entre com a peca para promocao (B/C/R/Q): ");
                        type = sc.nextLine().toUpperCase();

                    } 
                    partidaXadrez.substituirPeca(type);
                }
            }
            catch (XadrezException e) {

                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }

        }
        UI.clearScreen();
        UI.printPartida(partidaXadrez, capturadas);
    }
}
