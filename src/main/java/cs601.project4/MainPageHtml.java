package cs601.project4;

public class MainPageHtml {
    public static String getMainPageHtml(String id1,String picture1,String id2,String picture2,String id3,String picture3) {
        return String.format("""
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                  <html xmlns="http://www.w3.org/1999/xhtml">
                    <html>
                    <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <meta content="text/html; charset=iso-8859-2" http-equiv="Content-Type">
                    
                    <style>
                    
                    .mySlides {display:none;}
                    .content {
                       position: absolute;
                       text-align: center;
                       color: white;
                     }
                     
                    .top-left {
                      position: absolute;
                      top: 8px;
                      left: 100px;
                    }
                    
                    </style>
                    </head>
                    
                    <body>
                        <h3 class="center">You may like: </h3>
                        <div class="content1" style="max-width:500px">
                          <a href="/buyTicket?event_id=%s" ><img class="mySlides" src="/images?image_name=%s" style="width:200%%; border-radius: 10px;"></a>
                          <a href="/buyTicket?event_id=%s" ><img class="mySlides" src="/images?image_name=%s" style="width:200%%; border-radius: 10px;"></a>
                          <a href="/buyTicket?event_id=%s" ><img class="mySlides" src="/images?image_name=%s" style="width:200%%; border-radius: 10px;"></a>
                         </div>
                       
                        <script>
                        var myIndex = 0; 
                        carousel();
                        
                        function carousel() {
                          var i;
                          var x = document.getElementsByClassName("mySlides");
                          for (i = 0; i < x.length; i++) {
                            x[i].style.display = "none"; 
                          }
                          myIndex++;
                          if (myIndex > x.length) {myIndex = 1}   
                          x[myIndex-1].style.display = "block"; 
                          setTimeout(carousel, 2000); // Change image every 2 seconds
                        }
                        </script>
                        
                     </body>
                    </html>
                        """, id1,picture1,id2,picture2,id3,picture3);

    }
}
