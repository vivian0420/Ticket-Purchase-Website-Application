package cs601.project4;

public class HomeHtml {
    public static String getHomeHtml() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                  <html xmlns="http://www.w3.org/1999/xhtml">
                    <html>
                      <head>
                        <title>Add New Item</title>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <Style>
                        
                         body {
                           margin: 20;
                         }
                         .header {
                           min-height: 50% !important;
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
                         
                         .topnav {
                           overflow: hidden;
                           background-color: #778899;
                           padding: 8px 16px;
                         }
                         
                         form {
                           float: right;
                           margin-right: 40px;
                         }
                         
                         #newitem {
                           float: right;
                           margin-right: 40px;
                         }
                         
                         
                        </style>
                      </head>
                      <body>
                        <div class="header">
                           <img src="https://user-images.githubusercontent.com/86545567/142148861-96fd31fb-3999-4bc3-90bc-16a09a957bd9.jpg" alt="Flowers in Chania" width="200" height="200">
                           <h1 style="font-size:80px; margin-right: 200px;">Event For You</h1>
                        </div>
                        
                        <div class="topnav">
                         <form accept-charset="utf-8">
                            <input type="text" name="search" value=""/>
                            <button id='search' type='submit'>Search</button>
                         </form>
                         <button id="newitem">Add New Event</button>
                         <button onclick="location.href = 'http://localhost:8080/';" id="myButton" class="float-left submit-button" >Home</button>
                         </div>
                        
                      </body>
                    </html>
                  </html>
                """;
    }
}
