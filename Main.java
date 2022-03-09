import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        String inputString;
        int forced = 0; //dicteaza daca programul nostru are voie sa raspunda cu mutari proprii

        //sistemul de citire al comenzilor trimise de xboard
        Scanner input = new Scanner(System.in);
        inputString = input.nextLine();

        if(inputString.equals("xboard")) {
            while (input.hasNext()) {
                System.out.flush();
                inputString = input.nextLine();

                if(inputString.length() > 8 && inputString.startsWith("protover")) {
                    System.out.println("feature sigint=0");
                    System.out.println("feature san=0");
                    System.out.println("feature name=MEM chess engine");
                }

                if(inputString.equals("force")) {
                    forced = 1;
                }

                if(inputString.equals("white")) {
                    Logic.myColor = Color.WHITE;
                }
                if(inputString.equals("black")) {
                    Logic.myColor = Color.BLACK;
                }
                if(inputString.equals("go")) {
                    forced = 0;
                    Logic.generateMove();
                }
                if(inputString.equals("new")) {
                    Logic.init();
                    forced = 0;
                }
                if(inputString.equals("quit")){
                    System.exit(0);
                }
                //daca xboard trimite ca si comanda o mutare
                if((inputString.length() == 4 || inputString.length() == 5)
                        && Character.isDigit(inputString.charAt(3))) {

                    Character promotion = inputString.length() == 4 ? null : inputString.charAt(4);

                    Logic.makeMove(new Coordonate(inputString.substring(0,2)),
                            new Coordonate(inputString.substring(2,4)),promotion);

                    if(forced == 0) Logic.generateMove();
                    System.out.println("Current field: "+Logic.board);
                }
            }
        }
        input.close();
    }
}
