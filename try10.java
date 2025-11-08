import java.util.Arrays;
import java.util.Scanner;

public class try10{

    private static void printhead(int frames){
        System.out.print("ref | ");
        for(int i=1; i<= frames; i++) System.out.printf("frame%-2d" , i);
        System.out.println(" Status");
        System.out.println("------------" + "-".repeat(frames * 10) + "--------");
    }

    private static void printstat(int page, int[] mem, boolean hit){
        System.out.printf("%3d" , page);
        for(int v : mem) System.out.printf("  %s" , v == -1 ? "-" : Integer.toString(v));
        System.out.println(hit ? "Hit" : "Fault");
    }

    public static void fifo(int[] ref, int frames){
        int[] mem = new int[frames];
        int fault = 0;
        int hit = 0;
        int pointer = 0;
        Arrays.fill(mem , -1);

        System.out.println("============ FIFO OUTPUT =============");
        printhead(frames);

        for(int page : ref){
            boolean found = false;
            for(int i =0 ; i< frames ; i++){
                if(mem[i] == page) {
                    found = true;
                    hit++;
                    break;
                }
            }
            if(!found){
                mem[pointer] = page;
                pointer = (pointer + 1)% frames;
                fault++;
            }

            printstat(page , mem , found);
        }

        System.out.println("result");
        System.out.println("Hit" + hit);
        System.out.println("fault" + fault);
    }

    public static void optimal (int[] ref, int frames){
        int[] mem = new int[frames];
        Arrays.fill(mem, -1);
        int hit = 0;
        int fault = 0;

        System.out.println("\n=== Optimal (frames = " + frames + ") ===");
        printhead(frames);

        for(int i=0 ; i< ref.length; i++){

            boolean found = false;
            int page = ref[i];

            for(int j=0; j< frames; j++){
                if(mem[j] == page){
                    hit++;
                    found = true;
                    break;
                }
            }

            if(!found){
                fault++;
                int emptidx = -1;
                for(int j=0; j< frames; j++){
                    if(mem[j] == -1) {
                        emptidx = j;
                        break;
                    }
                }

                if(emptidx != -1){

                    mem[emptidx] = page;
                }else{
                    int replaceidx = -1;
                    int farthest = -1;

                    for(int j=0 ; j<frames; j++){
                        int nextuse = Integer.MAX_VALUE;
                        for(int k=i+1; k<ref.length ; k++){
                            if(mem[j] == ref[k]){
                                nextuse = k;
                                break;
                            }
                        }

                        if(nextuse > farthest){
                            farthest = nextuse;
                            replaceidx = j; 
                        }
                    }
                    mem[replaceidx] = page;
                }
            }

            printstat(page, mem ,found);
        }

        System.out.println("result");
        System.out.println("Hit" + hit);
        System.out.println("fault" + fault);
    }


    public static void main (String []args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of frames ");
        int frames = sc.nextInt();

        System.out.println("Enter length of string ");
        int n = sc.nextInt();

        int[] reference = new int[n];
        System.out.println("Enter elements in reference string ");
        for(int i=0; i<n ; i++){
            reference[i] = sc.nextInt();
        }

        // fifo(reference, frames);
        optimal(reference, frames);

        sc.close();
    }
}