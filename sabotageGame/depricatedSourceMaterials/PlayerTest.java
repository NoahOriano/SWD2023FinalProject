import java.util.ArrayList;

public class PlayerTest {
    public static void main(String[] args) {
        Player elijah = new Player("Rocker");
        Player noah = new Player("Tall");
        Player joey = new Player("degen");
        Player rock = new Player("bot");

        ArrayList<Player> list = new ArrayList<>();
        list.add(elijah);
        list.add(noah);
        list.add(joey);
        list.add(rock);

        list.get(1).setImposter(true);

        joey.createPlayerFiles(list,joey);
        noah.createPlayerFiles(list,noah);
        elijah.createPlayerFiles(list,elijah);
        rock.createPlayerFiles(list,rock);

        System.out.println(joey.getPlayerFiles().get(0).getSuspect().getProfileName());
        System.out.println(joey.getPlayerFiles().get(1).getSuspect().getProfileName());
        System.out.println(joey.getPlayerFiles().get(2).getSuspect().getProfileName());
        System.out.println(joey.getPlayerFiles().get(3).getSuspect().getProfileName());

EvidenceFile testing = new EvidenceFile(joey,joey);


    }
}
