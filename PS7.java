// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.util.Scanner;

public class PS7 {
   public PS7() {
   }

   static void printTable(int[] var0, int[] var1, int[] var2, String var3) {
      System.out.println("\n===== " + var3 + " Allocation =====");
      System.out.println("Process\tProcess Size\tBlock No\tBlock Size\tUnused Space");
      System.out.println("---------------------------------------------------------------");

      for(int var4 = 0; var4 < var0.length; ++var4) {
         if (var2[var4] != -1) {
            int var5 = var2[var4];
            System.out.println("P" + (var4 + 1) + "\t\t" + var0[var4] + "\t\t" + (var5 + 1) + "\t\t" + var1[var5] + "\t\t" + (var1[var5] - var0[var4]));
         } else {
            System.out.println("P" + (var4 + 1) + "\t\t" + var0[var4] + "\t\tNot Allocated\t  -\t\t  -");
         }
      }

   }

   static void firstFit(int[] var0, int[] var1) {
      int[] var2 = new int[var1.length];
      int[] var3 = (int[])var0.clone();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2[var4] = -1;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5] >= var1[var4]) {
               var2[var4] = var5;
               var3[var5] -= var1[var4];
               break;
            }
         }
      }

      printTable(var1, var0, var2, "First Fit");
   }

   static void nextFit(int[] var0, int[] var1) {
      int[] var2 = new int[var1.length];
      int[] var3 = (int[])var0.clone();
      int var4 = 0;

      for(int var5 = 0; var5 < var1.length; ++var5) {
         var2[var5] = -1;

         for(int var6 = 0; var6 < var3.length; ++var6) {
            int var7 = (var4 + var6) % var3.length;
            if (var3[var7] >= var1[var5]) {
               var2[var5] = var7;
               var3[var7] -= var1[var5];
               var4 = var7;
               break;
            }
         }
      }

      printTable(var1, var0, var2, "Next Fit");
   }

   static void bestFit(int[] var0, int[] var1) {
      int[] var2 = new int[var1.length];
      int[] var3 = (int[])var0.clone();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2[var4] = -1;
         int var5 = -1;

         for(int var6 = 0; var6 < var3.length; ++var6) {
            if (var3[var6] >= var1[var4] && (var5 == -1 || var3[var6] < var3[var5])) {
               var5 = var6;
            }
         }

         if (var5 != -1) {
            var2[var4] = var5;
            var3[var5] -= var1[var4];
         }
      }

      printTable(var1, var0, var2, "Best Fit");
   }

   public static void main(String[] var0) {
      Scanner var1 = new Scanner(System.in);
      System.out.print("Enter number of memory blocks: ");
      int var2 = var1.nextInt();
      int[] var3 = new int[var2];
      System.out.println("Enter block sizes:");

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var3[var4] = var1.nextInt();
      }

      System.out.print("Enter number of processes: ");
      var4 = var1.nextInt();
      int[] var5 = new int[var4];
      System.out.println("Enter process sizes:");

      for(int var6 = 0; var6 < var4; ++var6) {
         var5[var6] = var1.nextInt();
      }

      firstFit(var3, var5);
      nextFit(var3, var5);
      bestFit(var3, var5);
      var1.close();
   }
}
