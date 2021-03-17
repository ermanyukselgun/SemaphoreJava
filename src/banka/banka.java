





import java.util.concurrent.Semaphore;
import java.util.Scanner;
import java.util.Random;

public class banka 
{
	static Random rand = new Random();
	
	// 2 banka ve hesaplar
	static int banka[][];
	
	// bankalarin hesap sayisi : {5,6 ..., 15} 
	static int hesap_sayisi = 5 + rand.nextInt(11);	

	public static void Banka_Olusturma()
	{
		
		System.out.println("Bankalarin hesap sayisi: " + hesap_sayisi);
		
		// her bankanin esit miktarda hesap sayisi mevcuttur
		banka = new int[2][hesap_sayisi];	

		// hesaplardaki para miktarlarini yazalim
        for(int i = 0; i < hesap_sayisi; i++)
        {
            banka[0][i] = rand.nextInt(21);
            banka[1][i] = rand.nextInt(21);
        }

        System.out.println("Banka 0 hesaplari: ");
        for(int i = 0; i < hesap_sayisi; i++)
        	System.out.print(banka[0][i] + " ");
        System.out.println();
        
        System.out.println("Banka 1 hesaplari: ");
        for(int i = 0; i < hesap_sayisi; i++)
        	System.out.print(banka[1][i] + " ");
        System.out.println();
	}
		
	static Semaphore sem = new Semaphore(1);
	static boolean sem_bank0 = false;
	static boolean sem_bank1 = false;

	
	static class islem extends Thread 
	{
            
           
		String islem_ismi;
		int bankadan;
		int kimden;
		int bankaya;		
		int kime;
		int miktar;
		

		public islem(String isim, int bankadan,  int kimden, int bankaya, int kime, int miktar)
		{
			this.islem_ismi = isim;
			this.bankadan = bankadan;
			this.kimden = kimden;
			this.bankaya = bankaya;			
			this.kime = kime;
			this.miktar = miktar;
                        
		}
		
		public void run() {

			try 
			{
				System.out.println("Thread Id: "+Thread.currentThread().getId()+" "+islem_ismi + " : izin alinmaya calisiliyor..." );
								
				while(true)
				{
					/* ALAN 2 : her bir islemi senkronize edin
					 *        : islemler ayni banka uzerinde islem yapamazlar
			         *        : 50 puan
			         */
                                               
//                                    
//                             
					if(bankadan==0)
                                        {
                                            if (bankaya==0) 
                                            {
                                                try 
                                                {
                                                    sem.acquire();
                                                    if(sem_bank0==false)
                                                        {
                                                            sem_bank0 = true;
                                                            sem.release();
                                                            break;
                                                        }
                                                    else{sem.release();}
                                                } 
                                                catch (InterruptedException e) 
                                                {

                                                        e.printStackTrace();
                                                }
                                                
                                            }
                                            else if (bankaya ==1)
                                            {
                                                try
                                                {
                                                    sem.acquire();
                                                    if (sem_bank0 == false && sem_bank1 == false ) 
                                                        {
                                                            sem_bank0 = true ;
                                                            sem_bank1 = true ;
                                                            sem.release();
                                                            break;
                                                        }
                                                    else{sem.release();}
                                                }
                                                catch (InterruptedException e) 
                                                {

                                                        e.printStackTrace();
                                                }
                                                
                                            }
                                        }
                                        else if(bankadan==1)
                                        {
                                            if (bankaya == 1)
                                            {
                                                try 
                                                {
                                                    sem.acquire();  
                                                
                                                    if (sem_bank1 == false) 
                                                        {
                                                            sem_bank1 = true;
                                                            sem.release();
                                                            break;
                                                        }
                                                    else{sem.release();}
                                                } 
                                                catch (InterruptedException e) 
                                                {

                                                        e.printStackTrace();
                                                }
                                               
                                            }
                                            else if (bankaya == 0)
                                            {   
                                                try 
                                                {
                                                    sem.acquire();
                                                    if (sem_bank1 == false && sem_bank0 == false) 
                                                        {
                                                            sem_bank1 = true;
                                                            sem_bank0 = true;
                                                            sem.release();
                                                            break;                                                    
                                                        }
                                                    else{sem.release();}
                                                } 
                                                catch (InterruptedException e) 
                                                {

                                                        e.printStackTrace();
                                                }
                                                
                                                
                                                
                                            }
                                            
                                        }
					

					/* */
				}
				

				System.out.println(islem_ismi + " : izni aldik");

				try 
				{
					System.out.println(islem_ismi + " : islem yapiyorum ");
					Thread.sleep(1000);	
					/* ALAN 3 : burada istenilen bankadaki hesaptan 
					 *        :        istenilen bankadaki hesaba 
					 *                 "miktar" kadar para aktarin
					 *                 ve ekrana bastirin
					 *        : 20 puan
					 */
                                        
                                        
                                        banka[bankadan][kimden]-= miktar ;
                                        System.out.println(" Banka " + bankadan + " - Hesap " + kimden + " miktar:" + miktar + " cekildi" + " hesap durumu : " + banka[bankadan][kimden] );
                                        
                                        banka[bankaya][kime] += miktar ;
                                        System.out.println(" Banka " + bankaya + " - Hesap " + kime + " miktar:" + miktar + " yatirildi" + " hesap durumu : " + banka[bankaya][kime] );
					/* */
					
					Thread.sleep(1000);	
				} finally {
					System.out.println(islem_ismi + " : izin birakiliyor...");
					
                                        	
					/* ALAN 4 : burada kilidi aldiktan sonra gerekli degiskenleri
					 *          eski haline getirin ki baska islemler de 
					 *          para transferi islemini gerceklestirsin
					 *        : 15 puan
					 */
                                        
                                        
                                        try 
                                        {   
                                            sem.acquire();
                                            if (bankadan==0 && bankaya==0) 
                                            {
                                                
                                                sem_bank0 = false;
                                                
                                            }
                                            else if (bankadan == 0 && bankaya ==1)
                                            {
                                                
                                                sem_bank0 = false;
                                                sem_bank1 = false;
                                                
                                            }
                                            else if (bankadan ==1 && bankaya ==0)
                                            {
                                                
                                                sem_bank0 = false;
                                                sem_bank1 = false;
                                                
                                            }
                                            else if (bankadan==1 && bankaya ==1)
                                            {
                                                
                                                sem_bank1 = false;
                                                
                                            }
                                            
                                            sem.release();

                                    } 
                                        catch (InterruptedException e) 
                                        {
                                                e.printStackTrace();
                                        }
                                        
					
					/* */
										

				}

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

			
		}
		
		
		
	}

	public static void main(String[] args)
	{
		Banka_Olusturma();
		
		// islem sayisi {7,8,9,10,11} sayilarindan biri olmalidir
        int islem_sayisi = rand.nextInt(5) + 7;
        islem [] islem_listesi = new islem[islem_sayisi];
        
        /*  her bir islemi olusturduk
         *        : hangi bankadaki hesaptan hangi bankadaki hesaba
         *          ne kadar para gidecegini rastgele belirttik 
         *          islem ismi bu bilgilerin yanyana - ile birlesimidir
         *     */
        for(int i = 0; i < islem_sayisi; i++)
        {
        	int bf = rand.nextInt(2), bt = rand.nextInt(2), 
            af = rand.nextInt(hesap_sayisi),
            at = rand.nextInt(hesap_sayisi), 
            amount = rand.nextInt(20);
//        	String isim = String.valueOf(bf) + "-" + String.valueOf(bt) + "-" + String.valueOf(af) + "-" + String.valueOf(at) + "-" + String.valueOf(amount);
        	String isim = String.valueOf(bf) + "-" + String.valueOf(af) + "-" + String.valueOf(bt) + "-" + String.valueOf(at) + "-" + String.valueOf(amount);
            islem_listesi[i] = new islem(isim,bf,af, bt,at,amount);
        }
        
        
        System.out.println("Islemler baslasin : " + islem_sayisi + " tane islem");   
        
        /* ALAN 1 : her bir islemi paralel calistirin 
         *        : 15 puan
         */
        
		
		/* */
                          
		          for (int i = 0; i < islem_sayisi; i++) 
                            {
                                islem_listesi[i].start();
                            }
		
    
		// "main thread" diger islemlerin bitmesini beklesin
		for (Thread thread : islem_listesi) 
			try 
            {
				thread.join();
            } catch (InterruptedException e)  
            {
                Thread.currentThread().interrupt(); 
                
            }
		
	
         
		System.out.println("islemler bitti");
	}	

}

