package cs601.project4;

public class LoginPageHtml {
    public static String getLoginHtml() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                  <html xmlns="http://www.w3.org/1999/xhtml">
                    <html>
                      <head>
                        <title>EventForYou</title>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <style>
                         body {
                           margin: 20;
                         }
                         .header {
                           background-color: #B0E0E6;
                           padding: 10px;
                           text-align: center;
                           font-family: Apple Chancery;
                         }
                         
                         img{
                           border-radius: 100px;
                           float: left;
                           margin-right: 80px;
                           margin-left: 80px;
                         }
                         
                         #login {
                           margin-left: 40px;
                         }
                         
                         .topnav {
                           overflow: hidden;
                           background-color: #778899;
                           padding: 8px 16px;
                         }
                         
                         .column {
                           float: left;
                           width: 30%;
                           padding: 15px;
                         }
                         
                         /* Clearfix (clear floats) */
                         .row::after {
                           content: "";
                           clear: both;
                           display: table;
                         }
                        </style>
                      </head>
                      <body>
                        <div class="header">
                           <img src="https://user-images.githubusercontent.com/86545567/142148861-96fd31fb-3999-4bc3-90bc-16a09a957bd9.jpg" alt="Logo" width="200" height="200">
                           <h1 style="font-size:80px; margin-right: 200px;">Event For You</h1>
                        </div>
                        
                        <div class="topnav">
                           <button id="login">Login</button>
                        </div>
                        
                        <div class="row">
                          <div class="column">
                            <h2 style="font-size:50px;margin-left: 50px;font-family: Cursive;">Enjoy your events with us</h2>
                            <img src="https://user-images.githubusercontent.com/86545567/142331710-70b0975e-4365-42cc-82c5-8745210b3276.jpeg"" alt="Party" style="width:100%;">
                          </div>
                          <div class="column">
                            <img src="https://user-images.githubusercontent.com/86545567/142333447-63fd7954-8b3a-404b-b093-57cdc3ecfb2d.png" alt="Running" style="width:100%; margin-top: 30px; margin-right: 0px;">
                            <img src="https://user-images.githubusercontent.com/86545567/142334592-92f3ede1-09cd-45ac-87d1-133c1ed87373.gif" alt="coffee" style="width:100%; margin-top: 30px;">
                          </div>
                          <div class="column">
                            
                            <img src="https://user-images.githubusercontent.com/86545567/142339059-17c9ea96-4002-45f5-a8d8-2a8e7884b6c3.jpeg" alt="coffee" style="width:200; height: 325px;margin-top: 150px; margin-right: 10px;">
                            
                        </div>
                        
                        
                      </body>
                    </html>
                  </html> 
                      
                """;
    }
}
