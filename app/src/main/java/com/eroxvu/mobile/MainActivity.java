package com.eroxvu.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(new GameView());
    }

    class GameView extends View {
        Paint p = new Paint(3);
        float px=0, py=0, joyX=0, joyY=0, downX=0, downY=0;
        boolean joy=false, inCar=false;
        long last=System.nanoTime();
        String notice="Добро пожаловать в Рязань";
        float scale=1f;

        GameView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE,null); }

        void color(int c){ p.setColor(c); p.setStyle(Paint.Style.FILL); }
        void text(Canvas c,String s,float x,float y,float size,int col){
            color(col); p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextSize(size); c.drawText(s,x,y,p);
        }
        @Override protected void onDraw(Canvas real) {
            super.onDraw(real);
            float w=getWidth(), h=getHeight();
            float dt=Math.min(.05f,(System.nanoTime()-last)/1e9f); last=System.nanoTime();
            if(joy && !inCar){ px += joyX*180*dt; py += joyY*180*dt; }
            Canvas c=real;
            c.drawColor(Color.rgb(105,145,116));
            c.save();
            c.translate(w*.5f-px,h*.52f-py);
            // Городская сетка и кварталы
            for(int x=-1200;x<=1200;x+=420) for(int y=-1000;y<=1000;y+=360){
                color(Color.rgb(84,112,91)); c.drawRoundRect(x+35,y+35,x+385,y+325,18,18,p);
                color(Color.rgb(191,190,177)); c.drawRoundRect(x+75,y+65,x+345,y+295,12,12,p);
                color(Color.rgb(154,169,178)); c.drawRoundRect(x+90,y+80,x+330,y+280,8,8,p);
                color(Color.rgb(91,119,133)); c.drawRect(x+105,y+95,x+315,y+265,p);
                color(Color.rgb(221,214,185)); c.drawRect(x+35,y+165,x+385,y+195,p);
            }
            // Дороги
            color(Color.rgb(55,59,64)); c.drawRect(-1400,-95,1400,95,p);
            c.drawRect(-95,-1200,95,1200,p);
            color(Color.rgb(232,213,133)); p.setStrokeWidth(4); p.setStyle(Paint.Style.STROKE);
            for(int x=-1300;x<1300;x+=70) c.drawLine(x,0,x+35,0,p);
            for(int y=-1100;y<1100;y+=70) c.drawLine(0,y,0,y+35,p);
            p.setStyle(Paint.Style.FILL);
            // Вокзал
            color(Color.rgb(222,222,215)); c.drawRoundRect(-270,-370,270,-220,20,20,p);
            color(Color.rgb(65,112,143)); c.drawRect(-245,-345,245,-315,p);
            text(c,"ВОКЗАЛ РЯЗАНЬ",-180,-270,27,Color.rgb(30,55,72));
            // Деревья
            for(int i=0;i<12;i++){
                float tx=-600+(i*113)%1200, ty=260+(i*67)%650;
                color(Color.rgb(67,105,65)); c.drawCircle(tx,ty,24,p);
                color(Color.rgb(104,75,53)); c.drawRect(tx-4,ty+10,tx+4,ty+34,p);
            }
            // Машина
            color(Color.rgb(26,121,164)); c.drawRoundRect(135,-42,235,42,18,18,p);
            color(Color.rgb(180,222,235)); c.drawRoundRect(158,-28,212,28,9,9,p);
            text(c,"LADA",157,8,13,Color.WHITE);
            // Игрок
            if(!inCar){
                color(Color.rgb(34,49,62)); c.drawCircle(0,0,18,p);
                color(Color.rgb(240,193,151)); c.drawCircle(0,-7,10,p);
                color(Color.rgb(30,116,171)); c.drawRoundRect(-13,8,13,34,8,8,p);
            } else {
                color(Color.rgb(20,155,190)); c.drawRoundRect(-35,-22,35,22,12,12,p);
                color(Color.WHITE); c.drawCircle(0,0,6,p);
            }
            c.restore();

            // Верхняя панель
            color(0xCC101D2A); c.drawRoundRect(18,14,310,66,18,18,p);
            text(c,"EROXVU RP",34,46,22,Color.WHITE);
            text(c,"РЯЗАНЬ  •  ПРОТОТИП",34,62,10,0xFF75D8F5);
            color(0xCC101D2A); c.drawRoundRect(w-176,14,w-18,84,18,18,p);
            text(c,"РАДАР",w-153,36,12,0xFF8DE5FF);
            color(0xFF75D8F5); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(2);
            c.drawCircle(w-97,57,20,p); p.setStyle(Paint.Style.FILL);
            color(Color.WHITE); c.drawCircle(w-97,57,3,p);

            // Джойстик
            float jx=85, jy=h-92;
            color(0x66303F50); c.drawCircle(jx,jy,58,p);
            color(0xAA75D8F5); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(3); c.drawCircle(jx,jy,58,p); p.setStyle(Paint.Style.FILL);
            color(0xFFB8EFFF); c.drawCircle(jx+joyX*27,jy+joyY*27,22,p);
            // Action buttons
            button(c,w-82,h-92,48,"ДЕЙСТВИЕ");
            button(c,w-190,h-62,35,inCar?"ВЫЙТИ":"В МАШИНУ");
            button(c,w-82,h-205,37,"КАМЕРА");
            // status / toast-like hint
            color(0xCC101D2A); c.drawRoundRect(w*.34f,h-45,w*.66f,h-12,12,12,p);
            text(c,notice,w*.35f+8,h-23,12,Color.WHITE);
            invalidate();
        }

        void button(Canvas c,float x,float y,float r,String label){
            color(0xDD10283A); c.drawCircle(x,y,r,p);
            color(0xFF63CFF1); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(3); c.drawCircle(x,y,r,p); p.setStyle(Paint.Style.FILL);
            text(c,label,x-r+2,y+r+17,10,Color.WHITE);
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent e){
            float x=e.getX(), y=e.getY(), h=getHeight(), w=getWidth();
            int a=e.getActionMasked();
            if(a==MotionEvent.ACTION_DOWN || a==MotionEvent.ACTION_POINTER_DOWN){
                if(x<165 && y>h-175){ joy=true; downX=x; downY=y; updateJoy(x,y); }
                else if(x>w-145 && y>h-150){ inCar=!inCar; notice=inCar?"Вы сели в автомобиль":"Вы вышли из автомобиля"; }
                else if(x>w-145 && y>h-255 && y<h-155){ notice="Камера: режим прототипа"; }
                else if(x>w-245 && y>h-115){ notice="Действие пока в разработке"; }
                invalidate(); return true;
            }
            if(a==MotionEvent.ACTION_MOVE && joy){ updateJoy(x,y); return true; }
            if(a==MotionEvent.ACTION_UP || a==MotionEvent.ACTION_CANCEL || a==MotionEvent.ACTION_POINTER_UP){
                joy=false; joyX=joyY=0; invalidate(); return true;
            }
            return true;
        }
        void updateJoy(float x,float y){
            joyX=Math.max(-1,Math.min(1,(x-85)/48f));
            joyY=Math.max(-1,Math.min(1,(y-(getHeight()-92))/48f));
        }
    }
                }
