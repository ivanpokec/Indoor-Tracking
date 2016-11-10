using IndoorTracking.Models;
using IndoorTracking.Services;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace IndoorTracking.Controllers
{
    public class LoginController : ApiController
    {
        //User[] users = new User[] {
        //    new User {Id= 1, userName="test", passWord="test", name="pero"},
        //    new User {Id= 2, userName="test2", passWord="test2", name="pero2"}
        //};

        [HttpPost]
        public IHttpActionResult CheckLogin([FromBody] Login loginRequest)
        {
            string username = loginRequest.userName;
            string passWord = loginRequest.passWord;

            User logdUser = new User();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                
                string strCmd = @"SELECT kor_id, kor_username, kor_lozinka, kor_ime FROM korisnici WHERE kor_username=@username AND kor_lozinka=@lozinka";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@username", System.Data.SqlDbType.VarChar).Value= username;
                    cmd.Parameters.Add("@lozinka", System.Data.SqlDbType.VarChar).Value= passWord;
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        if (data.Read())
                        {
                            int id = 0;
                            bool check = Int32.TryParse(data["kor_id"].ToString(), out id);
                            if (check == true && id!=0)
                            {
                                logdUser.Id = id;
                                logdUser.userName = data["kor_username"].ToString();
                                logdUser.passWord = data["kor_lozinka"].ToString();
                                logdUser.name = data["kor_ime"].ToString();
                                return Ok(logdUser);
                            }
                            
                        }
                        
                        return NotFound();
                        
                    }
                }

            }

            //var user = users.FirstOrDefault((p) => p.userName == username && p.passWord == passWord);

            
        }
    }
}
