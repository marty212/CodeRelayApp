package com.example.myapp;

/**
 * Created by Martin on 11/8/2014.
 */
    public enum Challenge
    {
        //no clue how to implement pushups...
        DISTANCE(100), PUSHUP(2);

        int length;

        Challenge(int i)
        {
            length = i;
        }

        public int getLength() {
            return length;
        }

        public static Challenge getChallenge(int i)
        {
            switch(i)
            {
                case 0 : return DISTANCE;
                case 1 : return PUSHUP;
            }
            return null;
        }
    }
