package io.renren.common.utils;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
public class TaskClassification {
    public static enum CODE{
        READ("read",new int[]{33}),
        VIDEO("video",new int[]{34}),
        SIGN("sign",new int[]{40}),
        TIME("time",new int[]{32}),
        INVITATION("invitation",new int[]{37}),
        READINGS("readings",new int[]{25}),
        OTHER("other",new int[]{26,27,28,31,38,39,41,42,43,29,35,36,30});

        private String name;
        private int[] code;

        CODE(String name,int[] code){
            this.name=name;
            this.code=code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int[] getCode() {
            return code;
        }

        public void setCode(int[] code) {
            this.code = code;
        }

    }
}
