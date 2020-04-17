package com.max.tour.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class ClickableColorSpan extends ClickableSpan {
  
        private int color;
  
        public ClickableColorSpan(int color) {
            // this.str = str;  
            this.color = color;
        }  
  
        @Override  
        public void updateDrawState(TextPaint ds) {
            // ds.setColor(ds.linkColor);
            ds.setColor(color);
            ds.setUnderlineText(false);//是否显示下划线
        }  
    }  