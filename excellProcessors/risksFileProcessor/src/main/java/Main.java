public class Main {
    public static void main(String[] args) {
        RisksExtractor risksExtractor = new RisksExtractor("src/main/resources/risks.xlsx");
        RisksDTO risksDTO = risksExtractor.extract();

        System.out.println(risksDTO);
    }
}
