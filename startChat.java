/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author micha
 */
public class startChat {
    public static void main(String [] args){
        try{
            System.out.println("Hello ");        	        
            chatGroupUI c=new chatGroupUI();
            c.setVisible(true);
            Thread t1=new Thread((Runnable) c);
            t1.start();
       }catch(Exception e){e.printStackTrace();}
        
    }
}
