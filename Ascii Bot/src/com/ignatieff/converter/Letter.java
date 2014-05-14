package com.ignatieff.converter;

import java.awt.Color;

public class Letter {
	public static Color[] ALPHA = {new Color(219,219,219), //A
		new Color(217,217,217), //B
		new Color(228,228,228), //C
		new Color(221,221,221), //D
		new Color(217,216,217), //E
		new Color(223,222,223), //F
		new Color(222,222,222), //G
		new Color(218,218,218), //H
		new Color(217,217,217), //I
		new Color(231,231,231), //J
		new Color(218,217,217), //K
		new Color(227,226,227), //L
		new Color(211,210,211), //M
		new Color(215,214,214), //N
		new Color(224,224,224), //O
		new Color(222,222,222), //P
		new Color(216,216,216), //Q
		new Color(219,218,218), //R
		new Color(221,221,221), //S
		new Color(223,223,223), //T
		new Color(224,223,223), //U
		new Color(226,226,226), //V
		new Color(214,214,214), //W
		new Color(219,218,218), //X
		new Color(225,225,225), //Y
		new Color(222,221,222), //Z
		new Color(226,225,226), //a
		new Color(222,221,222), //b
		new Color(231,231,231), //c
		new Color(221,221,221), //d
		new Color(227,226,226), //e
		new Color(226,226,226), //f
		new Color(219,218,218), //g
		new Color(222,222,222), //h
		new Color(232,232,232), //i
		new Color(230,229,230), //j
		new Color(220,219,220), //k
		new Color(232,232,232), //l
		new Color(218,218,218), //m
		new Color(226,226,226), //n
		new Color(230,229,230), //o
		new Color(220,219,219), //p
		new Color(219,219,219), //q
		new Color(232,232,232), //r
		new Color(225,225,225), //s
		new Color(232,231,232), //t
		new Color(228,228,228), //u
		new Color(231,230,230), //v
		new Color(222,221,221), //w
		new Color(223,223,223), //x
		new Color(226,226,226), //y
		new Color(226,226,226), //z
		new Color(227,227,227), //0
		new Color(234,234,234), //1
		new Color(227,226,226), //2
		new Color(229,229,229), //3
		new Color(226,226,226), //4
		new Color(228,228,228), //5
		new Color(227,226,226), //6
		new Color(234,234,234), //7
		new Color(224,223,223), //8
		new Color(226,226,226), //9
		new Color(221,220,220), //&
		new Color(247,247,247), //.
		new Color(245,245,245), //,
		new Color(235,235,235), //?
		new Color(241,241,241), //!
		new Color(215,214,214), //@
		new Color(235,235,235), //(
		new Color(235,235,235), //)
		new Color(221,220,221), //#
		new Color(217,217,217), //$
		new Color(221,220,220), //%
		new Color(236,236,236), //*
		new Color(240,239,240), //+
		new Color(239,239,239), //-
		new Color(238,238,238), //=
		new Color(241,241,241), //:
		new Color(239,239,239),  //;
		new Color(255,255,255) //SPACE
							};
	
	
	public static double getDensity(int i){
		return 1-Segmentation.getDensity(ALPHA[i]);
	}

	public static char getBestChar(double density){
		double min=2, max=0;
		
		for(int i=0;i<ALPHA.length;i++){
			if(getDensity(i)<min)min=getDensity(i);
			if(getDensity(i)>max)max=getDensity(i);
		}
		
		//Find the one with lowest deviation
		double dd=0;
		int index=-1;
		for(int i=0;i<ALPHA.length;i++){
			double dx = Math.abs((getDensity(i)-min)/max-density);
			if(dx>dd){
				
				index=i;
				dd=dx;
			}
		}
		
		//return the lowest deviating
		return convertLetter(index);
		
		
	}
	
	public static char convertLetter(int i){
		int index = i;
		if(index<=25)return (char)(65+index);
		if(index<=51)return (char)(71+index);
		if(index<=61)return (char)(index-4);
		index -= 62;
		switch(index){
			case 0:
				return '&';
			case 1:
				return '.';
			case 2:
				return ',';
			case 3:
				return '?';
			case 4:
				return '!';
			case 5:
				return '@';
			case 6:
				return '(';
			case 7:
				return ')';
			case 8:
				return '#';
			case 9:
				return '$';
			case 10:
				return '%';
			case 11:
				return '*';
			case 12:
				return '+';
			case 13:
				return '-';
			case 14:
				return '=';
			case 15:
				return ':';
			case 16:
				return ';';
		}
		return ' ';
	}
}
