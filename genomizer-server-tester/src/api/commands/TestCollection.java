package api.commands;

import java.util.ArrayList;

/**
 * Super class for the classes which contains the different tests.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public abstract class TestCollection {

    //Used to keep track of how many tests was run and succeeded.
    static public ArrayList<String> nameOfFailedTests = new ArrayList<>();
    static public int runTests = 0;
    static public int succeededTests = 0;
    static public int failedTests = 0;

    protected ArrayList<SuperTestCommand> commandList;

    /**
     * Creates the list containing all the commands to test.
     */
    public TestCollection(){
        this.commandList = new ArrayList<>();
    }


    /**
     * Should be implemented to execute all the commands in the commandList.
     * @return true if all the commands in the commandList succeeded, otherwise
     *          false.
     */
    public abstract boolean execute();

    //Used as settings for the test run.
    protected static long delay = 0;
    protected static int laps = 1;
    protected static
    String garbage = "Bａｃｏԉ ìｐѕûϻ ԁ߀ɭ߀ｒ ａϻéｔ ｓｈ߀ùɭｄｅｒ ѕüлｔ ԉｏл ｉԉ. Sɑɭåϻì ｆɭåлƙ ｑûì ѵｅｎíäｍ åùｔë éɭïｔ ρ߀ｒｋ ｌｏïԉ êïｕｓｍòｄ. Bｒéѕɑòɭɑ íｎ ϻｏɭɭïｔ ìｎｃïԁïᏧùｎｔ ｐｒｏｓｃíüｔｔ߀ ｃûɭρå, ϻâǥԉå ɑᏧｉｐｉｓïｃｉлǥ ƙïｅɭｂáｓå ｖèԉｉｓｏл åｌｉｑúｉρ ｃòлｓëｑûãｔ. Rｉƃêｙé ɋｕì ｃúｐｉϻ ｌａлｄյäｅɢëｒ ƃ߀ｕᏧíԉ ｐàｒｉɑｔùｒ ｅｎｉϻ, ｓêｄ ｔåｉɭ ｋëｖｉｎ.\n" +
            "\n" +
            "Nｉѕï ｓｈｏｒｔ ｌ߀ｉл ѵｅɭｉｔ ｃ߀лｓｅｑùãｔ êâ ïρѕûｍ. Síｒɭｏíл յｏɯɭ ｆãｔƅɑｃｋ éхｃêρｔèúｒ ｃ߀ｎѕéɋúàｔ ìｒｕｒê. LäｎᏧϳâëｇêｒ ｉԉ ɭòｒｅϻ ｇｒｏùｎᏧ ｒｏùԉｄ ｓíｒｌòïл áԉｄòûｉɭɭé Ꮷèｓêｒüлｔ ѕｈｏｒｔ ｒìｂѕ ѵ߀ｌüｐｔäｔｅ ｔäíｌ. Iлｃïｄïｄｕԉｔ ｃúｌｐɑ ｃùρïｍ ѕüԉｔ, ǥｒ߀úлԁ ｒｏüлԁ ｉｄ ƙíéｌｂàｓâ ƃìɭｔｏｎǥ ρòｒƙ ƃｅɭɭϒ éлìｍ áɭｃäｔｒɑ ｂｏùԁïл. Oｃｃａêｃäｔ ëｌïｔ ｈåϻ, ϻàɢлà ｒùｍρ ｌåｂ߀ｒｉѕ ɭãｂｏｒûｍ ԉûｌɭá ｔ-ƅ߀ԉë ｃìｌｌùϻ ｃｈｕｃƙ ρｒòѕｃíûｔｔò ｉлｃïᏧìｄúｎｔ. LàｎᏧｊɑｅǥéｒ ｆïɭèｔ ｍìｇл߀ｎ հâｍƅüｒɢëｒ ｆｌàԉƙ, ƅ߀ｕԁïｎ ƃêèｆ ƅｒèｓàｏｌâ ѕｔｒìｐ ѕｔëàｋ ｃòɰ ｖêｎｉäϻ ϳ߀ｗɭ ѕíｒɭｏìԉ ｌòｒéϻ. Sìｎｔ ѕüлｔ ｉｎｃìᏧìｄùԉｔ ƅééｆ ｒｉƅѕ ѕìｒɭ߀íｎ ｎòｎ ｎûɭｌà ｅｓｔ ｂíｌｔｏｎｇ.\n" +
            "\n" +
            "Lｏｒｅｍ ｉｎ ｓｔｒíρ ｓｔèäƙ ѕհòｒｔ ｌ߀ïл êлïｍ ｐｒｏѕｃìｕｔｔｏ ｃòｗ ѵｅԉìｓ߀ԉ ｔòԉｇｕé ｓìｎｔ. Pãѕｔｒãϻï ｌâлᏧյåêǥèｒ ｐｏｒƙ ƃｅｌɭϒ ｃｏɰ êｉûѕｍòｄ ｔêԉｄêｒɭ߀ïԉ ｔｅｍρòｒ ｋíéｌƃａｓå. Cａｐïｃｏｌá ｃúｐìｍ ρäԉｃｅｔｔå ｐòｒｋ ｂèｌｌｙ ｔàíｌ. Cａｐìｃｏɭá ｐíｃáлɦɑ üｔ ｄｏɭòｒ. Dò ａлｄ߀üïｌｌｅ ｌｏｒｅｍ ρâｒïäｔûｒ ìｒｕｒé. Pｏｒｋ ｂéｌɭｙ ｐòｒƙ ｃɦòｐ Ꮷ߀ äɭｉɋúïｐ ƅëêｆ ｒìƃｓ ｔ-ｂ߀ｎé ɭàｂｏｒûｍ äｎｉｍ ｐｉｃãԉｈã ｒｕｍρ.";

}
