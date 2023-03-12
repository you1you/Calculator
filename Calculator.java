import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class Calculator {

    static Character[] definedStrArr = {'+', '.','O','1','2','3','-','4','5','6','7','8','9','/','*','(',')','%','='};
    static List<StringBuilder> historyQue = new ArrayList<>();
    static List<StringBuilder> redoQue = new ArrayList<>();

    /**
     * 检查输入字符是否合法
     * @param str
     * @return
     */
    public static StringBuilder checkInput(String str){
        List<Character> characterList = Arrays.asList(definedStrArr);
        for (int i = 0; i < str.length(); i++) {
            char inputChar = str.charAt(i);
            Integer index = characterList.indexOf(inputChar);
            if(index < 0){
                throw new RuntimeException(inputChar + "字符非法");
            }
            //计算结束符号
            if(inputChar == '='){
                if(i < str.length() - 1){
                    throw new RuntimeException("公示有误");
                }
            }
        }
        return new StringBuilder(str);
    }

    /**
     * 计算结果
     * @param str
     * @return
     * @throws ScriptException
     */
    public static String getResult(String str) throws ScriptException {
        Object resultObj = calc(str);
        return resultObj.toString();
    }

    /**
     * 公式计算
     * @param evalStr
     * @return
     */
    public static String eval(String evalStr){


        //括号数组
        List<String> bracketsList = new ArrayList<>();
        //第一个括号开始
        Integer firstBracketsIndex = evalStr.indexOf("(");
        //最后一个括号结束
        Integer endBracketsIndex = evalStr.lastIndexOf(")");
        //得出有括号的数据
        evalStr.substring(firstBracketsIndex,endBracketsIndex);
        //首先计算括号，再从做到右边计算
        return null;
    }

    /**
     * 计算
     * @param calcStr
     * @return
     */
    public static String calc(String calcStr){
        Deque<Integer> stack = new ArrayDeque<Integer>();
        Integer num = 0;
        Character calcSign = '+';
        int n = calcStr.length();
        for (int i = 0; i < calcStr.length(); i++) {
            if(Character.isDigit(calcStr.charAt(i))){
                num = num * 10 + calcStr.charAt(i) - '0';
            }

            if (!Character.isDigit(calcStr.charAt(i)) && calcStr.charAt(i) != ' ' || i == n - 1) {
                switch (calcSign) {
                    case '+':
                        stack.push(num);
                        break;
                    case '-':
                        stack.push(-num);
                        break;
                    case '*':
                        stack.push(stack.pop() * num);
                        break;
                    default:
                        stack.push(stack.pop() / num);
                }
                calcSign = calcStr.charAt(i);
                num = 0;
            }
        }
        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        return result+"";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder calcStr = null;

        while (scanner.hasNext()){
            String charNum = scanner.next();
            //判断是否是undo
            if("undo".equals(charNum)){
                undo();
                String str = getFinalStr(historyQue);
                System.out.println("undo后公式："+str);
                continue;
            }
            //判断是否是redo
            if("redo".equals(charNum)){
                redo();
                String str = getFinalStr(historyQue);
                System.out.println("redo后公式："+str);
                continue;
            }
            checkInput(charNum);
            int endIndex = charNum.indexOf("=");
            if(endIndex > 0){
                charNum = charNum.substring(0,endIndex);
            }else if(endIndex == 0){
                break;
            }
            historyQue.add(checkInput(charNum));
            redoQue.clear();
            if(endIndex >= 0){
                break;
            }
        }
        String result = null;
        String finalCalcStr = getFinalStr(historyQue);
        try {
            result = getResult(finalCalcStr);
        } catch (ScriptException e) {

            throw new RuntimeException("计算出错！"+e);
        }
        System.out.println("计算结果为："+result);
    }

    /**
     * 获取最后需要计算的字符串
     * @param historyQue
     * @return
     */
    private static String getFinalStr(List<StringBuilder> historyQue) {
        StringBuilder resultStr = new StringBuilder();
        if(historyQue != null && historyQue.size() > 0){
            for (int i = historyQue.size() - 1; i >= 0; i--) {

            }
            for (StringBuilder stringBuilder : historyQue) {
                resultStr.append(stringBuilder);
            }
        }
        return resultStr.toString();
    }

    /**
     * undo方法
     */
    private static void undo() {
        if(historyQue != null && historyQue.size() > 0){
            redoQue.add(historyQue.get(historyQue.size() - 1));
            historyQue.remove(historyQue.size() - 1);
        }
    }


    /**
     * redo方法
     */
    private static void redo() {
        if(redoQue != null && redoQue.size() > 0){
            historyQue.add(redoQue.get(redoQue.size() - 1));
            redoQue.remove(redoQue.size() - 1);
        }
    }

}
