package cs601.project4;

public class HomeHtml {
    public static String getHomeHtml(String user,String columnRight) {
        return String.format("""
           <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
             <html xmlns="http://www.w3.org/1999/xhtml">
               <html>
                 <head>
                   <title>EventForYou</title>
                   <meta charset="utf-8">
                   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
                   <script src="https://apis.google.com/js/platform.js?onload=onLoad" async defer></script>
                   <meta name="google-signin-client_id" content="760872738319-lfla8lsl8s03eb6jdahksego1g9tdrfv.apps.googleusercontent.com">
                   <script>
                   
                   function signOut() {
                          var auth2 = gapi.auth2.getAuthInstance();
                          auth2.signOut().then(function () {
                            console.log('User signed out.');
                          });
                        }
                   // https://stackoverflow.com/questions/29815870/typeerror-gapi-auth2-undefined
                   function onLoad() {
                         gapi.load('auth2', function() {
                           gapi.auth2.init();
                         });
                       }
                   </script>
                   <meta name="viewport" content="width=device-width, initial-scale=1">
                   <Style>
                   
                    body {
                      margin: 20;
                    }
                    .header {
                      background-color: #B0E0E6;
                      height: 220px;
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
                     
                    }
                    
                    form {
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
                        width: 100%%; /* Full width */
                        height: 100%%; /* Full height */
                        overflow: auto; /* Enable scroll if needed */
                        background-color: rgb(0,0,0); /* Fallback color */
                        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
                    }
                    .modal-content {
                        background-color: #fefefe;
                        margin: auto;
                        padding: 20px;
                        border: 1px solid #888;
                        width: 60%%;
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
                      width: 100%%;
                      padding: 12px 20px;
                      margin: 8px 0;
                      display: inline-block;
                      border: 1px solid #ccc;
                    }
                    #confirm {
                      background-color: #B0E0E6;
                      color: black;
                      border: none;
                      text-align: center;
                      cursor: pointer;
                      padding: 14px 20px;
                      border: 2px solid #778899;
                      border-radius: 12px;
                    }
                    .leftnav {
                      float: left;
                      width: 10%%;
                      height: flex;
                      text-align: left;
                      background: #ccc;
                      padding: 20px;
                      color: black;
                    }
                        
                    .column.left {
                      float: left;
                      width: 15%%;
                      margin-top: 10px;
                      margin-bottom: 10px;
                      height: 400px;
                    }
                    
                    .column.right {
                      width: 85%%;
                      margin-top: 10px;
                      margin-bottom: 10px;
                      height: 400px;
                      overflow: scroll;
                    }
                    
                   </style>
                   
                   
                 </head>
                 <body>
                   <div class="header">
                      <img src="https://user-images.githubusercontent.com/86545567/142148861-96fd31fb-3999-4bc3-90bc-16a09a957bd9.jpg" alt="Flowers in Chania" width="200" height="200">
                      <h1 style="font-size:80px; margin-right: 200px;">Event For You</h1>
                   </div>
                   
                   <div class="topnav">
                    <text>Welcome %s  </text>
                    <a href="/logout" onclick="signOut();">Sign out</a>
                    
                   </div>
                   
                   <script type="text/javascript">
                    // Get the modal
                    var modal = document.getElementById("myModal");
                    // Get the button that opens the modal
                    var btn = document.getElementById("newEvent");
                    var span = document.getElementsByClassName("close")[0];
                    // When the user clicks the button, open the modal
                      
                      function show_create_event() {
                        $("#myModal").show();
                      }
                    // When the user clicks on <span> (x), close the modal
                      function close_create_event() {
                        $("#myModal").hide();
                      }
                    // When the user clicks anywhere outside of the modal, close it
                      window.onclick = function(event) {
                        if (event.target == modal) {
                          modal.style.display = "none";
                        }
                      }
                      
                    function set_timezone() {
                       $("#timezone").val(Intl.DateTimeFormat().resolvedOptions().timeZone);
                    }
                    
                    function form_update() {
                        $("#formAction").val('UPDATE');
                        $("#timezone").val(Intl.DateTimeFormat().resolvedOptions().timeZone);
                      }
                      function form_delete() {
                        $("#formAction").val('DELETE');
                        $("#timezone").val(Intl.DateTimeFormat().resolvedOptions().timeZone);
                      }
                    
                    function purchase() {
                        alert('data: ' + data["success"]);
                        window.location.href = "/buyTicket";
                    }
                    
                    
                 </script>
                 </div>
                 
                 <div class="row">
                   <div class="column left">
                     <p><a href="/home" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;" onclick="/home" >Home</a></p>
                     <p><a href="/account" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;" onclick="/account">Account</a></p>
                     <p><a href="/transaction" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;">Transaction</a></p>
                     <p><a href="/allEvents" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;">All events</a></p>
                     <p><a href="/myEvents" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;">My events</a></p>
                     <p><a id="newEvent" data-toggle="modal" href="#myModal" style="text-decoration:none; color:#000000; size: 10px; margin-left:20px;"  onclick="show_create_event()">Add new event</a></p>
                     
                     <!-- The Modal -->
                     <form action="/createEvent" method="post" enctype="multipart/form-data" >
                     <div id="myModal" class="modal">
                       <!-- Modal content -->
                       <div class="modal-content">
                          <input type="hidden" name="timezone" id="timezone" />
                          
                          <span class="close" data-dismiss="modal" onclick="close_create_event()">&times;</span>
                          <label id="name" for="eventName"><b>Name:</b></label>
                          <input id="eventName" type="text" placeholder="Enter event name" name="eventName" required>
                   
                          <label for="startTime"><b>Start time:</b></label>
                          <input type="datetime-local" id="starttime" name="starttime">
                          
                          <label for="endTime"><b>End time:</b></label>
                          <input type="datetime-local" id="endtime" name="endtime">
                            
                          <label for="capacity"><b>Capacity:</b></label>
                          <input id="capacity" type="number" placeholder="Enter capacity" name="capacity" required>
                          
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
                          
                          <label for="image"><b>Image:</b></label>
                          <input id="image" type="file" name="image" accept="image/*" />
                   
                          <button id="confirm" type="submit" onclick="set_timezone()">Confirm</button>
                     </div>
                    </div>
                   </div>
                   </form>
                   <div class="column right">
                       %s
                   </div>
                 </div>
                 </body>
               </html>
             </html>
           """, user, columnRight);
    }
}
