package com.bean;

/**
 * @author A_Yuan
 * @create 2021-06-24 20:43
 */
public class Student {
    private int flowID; //流水号
    private int type; //考试类型
    private String IDCard; //身份证号码
    private String examCard; //准考证号
    private String name; //学生姓名
    private String location; //区域
    private int grade; //成绩

    public Student() {
    }

    public Student(int flowID, int type, String IDCard, String examCard, String name, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.name = name;
        this.location = location;
        this.grade = grade;
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        System.out.println("-------------查询结果-------------");
        return "流水号：\t\t" + flowID + "\n四级/六级：\t" + type + "\n身份证号：\t" + IDCard + "\n准考证号：\t"
                + examCard + "\n学生姓名：\t" + name + "\n区域：\t\t" + location + "\n成绩：\t\t" + grade;
    }

}
