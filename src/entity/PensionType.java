package entity;

public class PensionType {
    private int pension_id;
    private String pension_type;

//    public enum Pension {
//        ULTRAALLINCLUSIVE("Ultra All Inclusive"),
//        EVERYTHINGINCLUDED("Everything Included"),
//        ROOMBREAKFAST("Room Breakfast"),
//        FULLPENSION("Full Pension"),
//        HALFPENSION("Half Pension"),
//        ROOMONLY("Room Only"),
//        FULLCREDITNOALCOHOL("Full Credit, No Alcohol");
//
//
//        private String name;
//
//        Pension(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return this.name;
//        }
//
//        public String toString() {
//            return name;
//        }
//    }
//
//    public Pension getPension_type(){
//        return pension_type;
//    }

    public String getPension_type() {
        return pension_type;
    }

    public void setPension_type(String pension_type) {
        this.pension_type = pension_type;
    }

    public int getPension_id() {
        return pension_id;
    }

    public void setPension_id(int pension_id) {
        this.pension_id = pension_id;
    }


}
