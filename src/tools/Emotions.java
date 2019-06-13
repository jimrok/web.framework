package tools;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Emotions {

	Pattern pattern = Pattern
			.compile("/::\\)|/::~|/::B|/::\\||/:8-\\)|/::lt|/::\\$|/::X|/::Z|/::\\.\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::gt|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:and-\\(|/:B-\\)|/:lt@|/:@gt|/::-O|/:gt-\\||/:P-\\(|/::\\.\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:ltWgt|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok");
	
	final private HashMap<String,String[]> em = new HashMap<String,String[]>();
	
	private Emotions() {
		
		        em.put("/::)",new String[] {"0", "[微笑]", "Smile"});
		        em.put("/::~",new String[] {"1", "[撇嘴]", "Grimace"});
		        em.put("/::B",new String[] {"2", "[色]", "Drool"});
		        em.put("/::|",new String[] {"3", "[发呆]", "Scowl"});
		        em.put("/:8-)",new String[] {"4", "[得意]", "Chill"});
		        em.put("/::lt",new String[] {"5", "[流泪]", "Sob"});
		        em.put("/::$",new String[] {"6", "[害羞]", "Shy"});
		        em.put("/::X",new String[] {"7", "[闭嘴]", "Silent"});
		        em.put("/::Z",new String[] {"8", "[睡]", "Sleep"});
		        em.put("/::.(",new String[] {"9", "[大哭]", "Cry"});
		        em.put("/::-|",new String[] {"10", "[尴尬]", "Awkward"});
		        em.put("/::@",new String[] {"11", "[发怒]", "Pout"});
		        em.put("/::P",new String[] {"12", "[调皮]", "Wink"});
		        em.put("/::D",new String[] {"13", "[呲牙]", "Grin"});
		        em.put("/::O",new String[] {"14", "[惊讶]", "Surprised"});
		        em.put("/::(",new String[] {"15", "[难过]", "Frown"});
		        em.put("/::+",new String[] {"16", "[酷]", "Cool"});
		        em.put("/:--b",new String[] {"17", "[冷汗]", "Tension"});
		        em.put("/::Q",new String[] {"18", "[抓狂]", "Crazy"});
		        em.put("/::T",new String[] {"19", "[吐]", "Puke"});
		        em.put("/:,@P",new String[] {"20", "[偷笑]", "Chuckle"});
		        em.put("/:,@-D",new String[] {"21", "[可爱]", "Joyful"});
		        em.put("/::d",new String[] {"22", "[白眼]", "Slight"});
		        em.put("/:,@o",new String[] {"23", "[傲慢]", "Smug"});
		        em.put("/::g",new String[] {"24", "[饥饿]", "Hungry"});
		        em.put("/:|-)",new String[] {"25", "[困]", "Drowsy"});
		        em.put("/::!",new String[] {"26", "[惊恐]", "Panic"});
		        em.put("/::L",new String[] {"27", "[流汗]", "Sweat"});
		        em.put("/::gt",new String[] {"28", "[憨笑]", "Laugh"});
		        em.put("/::,@",new String[] {"29", "[悠闲]", "Commando"});
		        em.put("/:,@f",new String[] {"30", "[奋斗]", "Strive"});
		        em.put("/::-S",new String[] {"31", "[咒骂]", "Scold"});
		        em.put("/:?",new String[] {"32", "[疑问]", "Doubt"});
		        em.put("/:,@x",new String[] {"33", "[嘘]", "Shhh"});
		        em.put("/:,@@",new String[] {"34", "[晕]", "Dizzy"});
		        em.put("/::8",new String[] {"35", "[疯了]", "Tormented"});
		        em.put("/:,@!",new String[] {"36", "[衰]", "Toasted"});
		        em.put("/:!!!",new String[] {"37", "[骷髅]", "Skull"});
		        em.put("/:xx",new String[] {"38", "[敲打]", "Hammer"});
		        em.put("/:bye",new String[] {"39", "[再见]", "Wave"});
		        em.put("/:wipe",new String[] {"40", "[擦汗]", "Relief"});
		        em.put("/:dig",new String[] {"41", "[抠鼻]", "DigNose"});
		        em.put("/:handclap",new String[] {"42", "[鼓掌]", "Clap"});
		        em.put("/:and-(",new String[] {"43", "[糗大了]", "Shame"});
		        em.put("/:B-)",new String[] {"44", "[坏笑]", "Trick"});
		        em.put("/:lt@",new String[] {"45", "[左哼哼]", "BahL"});
		        em.put("/:@gt",new String[] {"46", "[右哼哼]", "BahR"});
		        em.put("/::-O",new String[] {"47", "[哈欠]", "Yawn"});
		        em.put("/:gt-|",new String[] {"48", "[鄙视]", "Lookdown"});
		        em.put("/:P-(",new String[] {"49", "[委屈]", "Wronged"});
		        em.put("/::.|",new String[] {"50", "[快哭了]", "Puling"});
		        em.put("/:X-)",new String[] {"51", "[阴险]", "Sly"});
		        em.put("/::*",new String[] {"52", "[亲亲]", "Kiss"});
		        em.put("/:@x",new String[] {"53", "[吓]", "Wrath"});
		        em.put("/:8*",new String[] {"54", "[可怜]", "Whimper"});
		        em.put("/:pd",new String[] {"55", "[菜刀]", "Cleaver"});
		        em.put("/:ltWgt",new String[] {"56", "[西瓜]", "Melon"});
		        em.put("/:beer",new String[] {"57", "[啤酒]", "Beer"});
		        em.put("/:basketb",new String[] {"58", "[篮球]", "Basketball"});
		        em.put("/:oo",new String[] {"59", "[乒乓]", "PingPong"});
		        em.put("/:coffee",new String[] {"60", "[咖啡]", "Coffee"});
		        em.put("/:eat",new String[] {"61", "[饭]", "Rice"});
		        em.put("/:pig",new String[] {"62", "[猪头]", "Pig"});
		        em.put("/:rose",new String[] {"63", "[玫瑰]", "Rose"});
		        em.put("/:fade",new String[] {"64", "[凋谢]", "Wilt"});
		        em.put("/:showlove",new String[] {"65", "[示爱]", "Lip"});
		        em.put("/:heart",new String[] {"66", "[爱心]", "Heart"});
		        em.put("/:break",new String[] {"67", "[心碎]", "BrokenHeart"});
		        em.put("/:cake",new String[] {"68", "[蛋糕]", "Cake"});
		        em.put("/:li",new String[] {"69", "[闪电]", "Lightning"});
		        em.put("/:bome",new String[] {"70", "[炸弹]", "Bomb"});
		        em.put("/:kn",new String[] {"71", "[刀]", "Dagger"});
		        em.put("/:footb",new String[] {"72", "[足球]", "Soccer"});
		        em.put("/:ladybug",new String[] {"73", "[瓢虫]", "Ladybug"});
		        em.put("/:shit",new String[] {"74", "[便便]", "Poop"});
		        em.put("/:moon",new String[] {"75", "[月亮]", "Moon"});
		        em.put("/:sun",new String[] {"76", "[太阳]", "Sun"});
		        em.put("/:gift",new String[] {"77", "[礼物]", "Gift"});
		        em.put("/:hug",new String[] {"78", "[拥抱]", "Hug"});
		        em.put("/:strong",new String[] {"79", "[强]", "ThumbsUp"});
		        em.put("/:weak",new String[] {"80", "[弱]", "ThumbsDown"});
		        em.put("/:share",new String[] {"81", "[握手]", "Shake"});
		        em.put("/:v",new String[] {"82", "[胜利]", "Victory"});
		        em.put("/:@)",new String[] {"83", "[抱拳]", "Fight"});
		        em.put("/:jj",new String[] {"84", "[勾引]", "Beckon"});
		        em.put("/:@@",new String[] {"85", "[拳头]", "Fist"});
		        em.put("/:bad",new String[] {"86", "[差劲]", "Pinky"});
		        em.put("/:lvu",new String[] {"87", "[爱你]", "Love"});
		        em.put("/:no",new String[] {"88", "[NO]", "No"});
		        em.put("/:ok",new String[] {"89", "[OK]", "OK"});
		        
	}
	
	private static Emotions emotions = new Emotions();
	
	public static String emotionCodeToName(String text) {
		Matcher m = emotions.pattern.matcher(text);
		StringBuffer sb = new StringBuffer();
		 while (m.find()) {
			 String foundGroup = m.group();
			 
		     m.appendReplacement(sb, emotions.em.get(foundGroup)[1]);
		 }
		 m.appendTail(sb);
		 return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(Emotions.emotionCodeToName("知道了，/:lvu"));
	}
	
	

}
