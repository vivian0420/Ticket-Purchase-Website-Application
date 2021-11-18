package cs601.project4;

public class HomeHtml {
    public static String getHomeHtml() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                  <html xmlns="http://www.w3.org/1999/xhtml">
                    <html>
                      <head>
                        <title>EventForYou</title>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <Style>
                        
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
                         * {
                           box-sizing: border-box;
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
                         
                         #newEvent {
                           float: right;
                           margin-right: 40px;
                         }
                         
                         #myHome {
                           margin-left:40px;
                         }
                         
                         .modal {
                             display: none; /* Hidden by default */
                             position: fixed; /* Stay in place */
                             z-index: 1; /* Sit on top */
                             padding-top: 100px; /* Location of the box */
                             left: 0;
                             top: 0;
                             width: 100%; /* Full width */
                             height: 100%; /* Full height */
                             overflow: auto; /* Enable scroll if needed */
                             background-color: rgb(0,0,0); /* Fallback color */
                             background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
                         }
                         .modal-content {
                             background-color: #fefefe;
                             margin: auto;
                             padding: 20px;
                             border: 1px solid #888;
                             width: 60%;
                         }
                         .close {
                             color: #aaaaaa;
                             float: right;
                             font-size: 28px;
                             font-weight: bold;
                         } 
                         .close:hover,
                         .close:focus {
                             color: #000;
                             text-decoration: none;
                             cursor: pointer;
                         }
                         #eventName,#starttime,#endtime,#capacity,#add1,#add2,#city,#state,#zipcode,#price,#description{
                           width: 100%;
                           padding: 12px 20px;
                           margin: 8px 0;
                           display: inline-block;
                           border: 1px solid #ccc;
                           
                           
                         }
                         #confirm {
                           background-color: #B0E0E6;
                           color: black;
                           border: none;
                           text-align: center
                           cursor: pointer;
                           padding: 14px 20px;
                           border: 2px solid #778899;
                           border-radius: 12px;
                         }
                         .leftnav {
                           float: left;
                           width: 10%;
                           height: flex;
                           text-align: left;
                           background: #ccc;
                           padding: 20px;
                           color: black;
                         }
                         
                         .column {
                           float: left;
                           padding: 10px;
                           margin-left: 20px;
                         }
                             
                         .column.left {
                           width: 15%;
                           
                           margin-top: 10px;
                           margin-bottom: 10px;
                           height: 400px;
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
                         <button onclick="location.href = 'http://localhost:8888/';" id="myHome" class="float-left submit-button" >Home</button>
                         <button id="newEvent">Add New Event</button>
                         <!-- The Modal -->
                         <div id="myModal" class="modal">
                            <!-- Modal content -->
                            <div class="modal-content">
                               <span class="close">&times;</span>
                               <label id="name" for="eventName"><b>Name:</b></label>
                               <input id="eventName" type="text" placeholder="Enter event name" name="eventName" required>
                        
                               <label for="startTime"><b>Start time:</b></label>
                               <input type="datetime-local" id="starttime" name="starttime">
                               
                               <label for="endTime"><b>End time:</b></label>
                               <input type="datetime-local" id="endtime" name="endtime">
                                 
                               <label for="capacity"><b>Capacity:</b></label>
                               <input id="capacity" type="text" placeholder="Enter capacity" name="capacity" required>
                               
                               <label for="address1"><b>Address1:</b></label>
                               <input id="add1" type="text" placeholder="Enter address" name="add1" required>
                               
                               <label for="address2"><b>Address2:</b></label>
                               <input id="add2" type="text" placeholder="Enter address" name="add2">
                               
                               <label for="city"><b>City:</b></label>
                               <input id="city" type="text" placeholder="Enter city" name="city" required>
                               
                               <label for="state"><b>State:</b></label>
                               <input id="state" type="text" placeholder="Enter state" name="state" required>
                               
                               <label for="zipcode"><b>Zipcode:</b></label>
                               <input id="zipcode" type="text" placeholder="Enter zipcode" name="zipcode" required>
                               
                               <label for="price"><b>Price:</b></label>
                               <input id="price" type="text" placeholder="Enter price" name="price" required>
                               
                               <label for="description"><b>Description:</b></label>
                               <input id="description" type="text" placeholder="Enter description" name="description" >
                        
                               <button id="confirm" type="submit" >Confirm</button>
                         
                         
                        </div>
                        </div>
                        <script>
                         // Get the modal
                         var modal = document.getElementById("myModal");
                         // Get the button that opens the modal
                         var btn = document.getElementById("newEvent");
                         
                         var conf = document.getElementById("confirm");
                         conf.onclick = function(){
                           modal.style.display = "none";
                             $.ajax({
                               type: 'POST',
                               url: '/CreateEventServlet',
                               data: JSON.stringify ({
                               eventname: $("#eventName").val(),
                               starttime: $("#starttime").val(),
                               endtime: $("#endtime").val(),
                               capacity: $("#capacity").val(),
                               address1: $("#add1").val(),
                               address2: $("#add2").val(),
                               city: $("#city").val(),
                               state: $("#state").val(),
                               zipcode: $("#zipcode").val(),
                               price: $("#price").val(),
                               description: $("#description").val()
                             }),
                             success: function(data) {
                               alert('data: ' + data["success"]);
                               window.location.href = "http://localhost:8888/";
                               
                               $("#eventName").val('');
                               $("#starttime").val('');
                               $("#endtime").val('');
                               $("#capacity").val('');
                               $("#address1").val('');
                               $("#address2").val('');
                               $("#city").val('');
                               $("#state").val('');
                               $("#zipcode").val('');
                               $("#price").val('');
                               $("#description").val('');
                             },
                             contentType: "application/json",
                             dataType: 'json'
                           });
                         }
                         
                         var span = document.getElementsByClassName("close")[0];
                         // When the user clicks the button, open the modal
                           btn.onclick = function() {
                             modal.style.display = "block";
                           }
                         // When the user clicks on <span> (x), close the modal
                           span.onclick = function() {
                             modal.style.display = "none";
                           }
                         // When the user clicks anywhere outside of the modal, close it
                           window.onclick = function(event) {
                             if (event.target == modal) {
                               modal.style.display = "none";
                             }
                           }
                        </script>
                      </div>
                      
                      <div class="row">
                        <div class="column left">
                          <p><a href="http://localhost:8888/" style="text-decoration:none; color:#000000; size: 10px;">Home</a></p>
                          <p><a href="#" style="text-decoration:none; color:#000000; size: 10px;">Account</a></p>
                          <p><a href="#" style="text-decoration:none; color:#000000; size: 10px;">Transaction</a></p>
                          <p><a href="#" style="text-decoration:none; color:#000000; size: 10px;">All events</a></p>
                          
                        </div>
                        <div class="column right">
                        </div>
                        
                      
                      
                      </body>
                    </html>
                  </html>
                """;
    }
}
