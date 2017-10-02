package org.test.bookpub.chuzhong;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChuzhongGetFenbiao {
    public static void main(String[] args) {

        int itemSize = 7;
        File file = new File("E:\\瞄之政治\\资料ALL\\20170729备份\\教研\\20171002-20171008\\chuzhong.txt");
        try {
            List<String> originals = Files.readLines(file, Charsets.UTF_8);
            Map<Integer, String> subjects = Maps.newHashMap();
            List<String> lines = Lists.newArrayList();
            int o = 0;
            for (int i = 0; i < originals.size(); i++) {
                String s = originals.get(i);
                if (s.startsWith("学科"))
                    subjects.put(o++, s);
                else
                    lines.add(s);
            }
            List<List<String>> items = Lists.newArrayList();
            int totalLins = lines.size();
            int i = totalLins / 5; // 总个数
            int q = 0;
            for (int j = 0; j < i; j++) {
                String[] t = new String[5];
                for (int k = 0; k < 5; k++) {
                    String a = lines.get(5 * j + k);
                    if (a.startsWith("第")){
                        t[0] = a;
                        if (a.startsWith("第一次") || a.startsWith("第1次")) {
                            int n = j - 1;
                            int count = 0;
                            while (n >= 0 && items.get(n).size() != itemSize) {
                                count++;
                                n--;
                            }
                            int m = 1;
                            n++;
                            String xueke = null;
                            String huibaoren = null;
                            boolean isEnter = false;
                            while (n >= 0 && n < j) {
                                if (!isEnter){
                                    isEnter = true;
                                    String sub = subjects.get(q);
                                    xueke = sub.split(" ")[0].split(":")[1];
                                    huibaoren =  sub.split(" ")[1].split(":")[1];
                                    q++;
                                }
                                items.get(n).add(xueke);
                                items.get(n).add(huibaoren);
//                                items.get(n).add(String.valueOf(m));
                                n++;
                                m++;
                            }
                        }
                    }
                    if (a.startsWith("培训时间") || a.startsWith("开始时间"))
                        t[1] = a;
                    if (a.startsWith("培训地点") || a.startsWith("具体地点"))
                        t[2] = a;
                    if (a.startsWith("培训讲师") || a.startsWith("讲师姓名"))
                        t[3] = a;
                    if (a.startsWith("培训内容"))
                        t[4] = a;
                }

                System.out.println(t[4]);
                List<String> temp = Lists.newArrayList();
//                temp.add(t[0].replace("：", "").replace("，", "").replace(",", ""));
                temp.add(t[4].replace("培训内容:", "").replace("培训内容，", "").replace(",", ""));
                t[1] = t[1].replace("培训时间:", "").replace("培训时间，", "").replace("培训时间,", "").replace("，", "").replace("开始时间:", "");
                if (t[1].startsWith("8月") || t[1].startsWith("9月")) {
                    t[1] = t[1].replace("（", "").replace("）", "").replace("：", ":");
                    String date = t[1].substring(0, t[1].indexOf("日") + 1);
                    int month = Integer.parseInt(date.substring(0, 1));
                    int day = Integer.parseInt(date.substring(2, date.indexOf("日")));
                    temp.add(new org.joda.time.DateTime(2017, month, day, 0, 0).toString("yyyy/MM/dd"));
                    temp.add(t[1].substring(t[1].indexOf("日") + 1));
                }else if (t[1].startsWith("2017")){
                    temp.add(t[1].substring(0, 9));
                    temp.add(t[1].substring(9).trim());
                }
                temp.add(t[2].replace("培训地点：", "").replace("培训地点，", "").replace("具体地点:", "").replace("培训地点:", ""));
                temp.add(t[3].replace("培训讲师：", "").replace("培训讲师，", "").replace("讲师姓名:", "").replace("培训讲师:", ""));

                items.add(temp);
            }
            int count = 0;
            int index = items.size() - 1;
            while (index > -1 && items.get(index).size() != itemSize) {
                count++;
                index--;
            }

            index++;
            int suoyin = 1;
            while (index < items.size()) {
                String sub = subjects.get(q);
                items.get(index).add(sub.substring(1).split(" ")[0].split(":")[1]);
                items.get(index).add(sub.substring(1).split(" ")[1].split(":")[1]);
//                items.get(index++).add(String.valueOf(suoyin));
                index++;
                suoyin++;
            }

            writeExcel("E:\\瞄之政治\\资料ALL\\20170729备份\\教研\\20171002-20171008\\chuzhongfenbiao.xls", items);




            System.out.println(items.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**生成一个Excel文件
     * @param fileName  要生成的Excel文件名
     */
    public static void writeExcel(String fileName, List<List<String>> items){
        WritableWorkbook wwb = null;
        try {
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
            wwb = Workbook.createWorkbook(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(wwb!=null){
            //创建一个可写入的工作表
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = wwb.createSheet("sheet1", 0);

            //下面开始添加单元格
            int i = 1;
            for(List<String> s : items){
                int j = 1;
                for(String t : s){
                    //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
                    Label labelC = new Label(j++, i, t);
                    try {
                        //将生成的单元格添加到工作表中
                        ws.addCell(labelC);
                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }

                }
                i++;
            }

            try {
                //从内存中写入文件中
                wwb.write();
                //关闭资源，释放内存
                wwb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }

}
