using IndoorTracking.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Data.SqlClient;
using IndoorTracking.Services;

namespace IndoorTracking.Controllers
{
    public class UserController : ApiController
    {
        //User[] users = new User[] {
        //    //new User {Id= 1, userName="test", passWord="test", name="pero"},
        //    //new User {Id= 2, userName="test2", passWord="test2", name="pero2"}
        //};
        public IEnumerable<User> GetAllUsers()
        {
            //IEnumerable<User> users = new List<User>();
            //UserCollection uc = new UserCollection();
            //uc.Add(new Models.User());

            //return uc;


            List<User> users = new List<User>();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT kor_id, kor_username, kor_lozinka, kor_ime FROM korisnici";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    //cmd.Parameters.Add("@pa1",System.Data.SqlDbType.VarChar).Value="test";
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            
                            //Filati strukturu users
                            User userRead = new User();
                            userRead.Id = int.Parse(data["kor_id"].ToString());
                            userRead.userName = data["kor_username"].ToString();
                            userRead.passWord = data["kor_lozinka"].ToString();
                            userRead.name = data["kor_ime"].ToString();
                            users.Add(userRead);
                        }
                    }
                }
                
            }

            return users;
        }
        //public IHttpActionResult GetUser(int id)
        //{
        //    var user = users.FirstOrDefault((p) => p.Id == id);

        //    if (user == null)
        //    {
        //        return NotFound();
        //    }
        //    return Ok(user);
        //}



    }
}
