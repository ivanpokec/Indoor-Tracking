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

        [HttpPost]
        public IHttpActionResult GetUser([FromBody] UsrId userIdRequest)        
        {
            User user = new User();

            //List<User> users = new List<User>();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT kor_id, kor_username, kor_lozinka, kor_ime, trenutna.lok_naziv AS trenutna_lokacija, uobicaj.lok_id, uobicaj.lok_naziv AS kor_lokacija, kat_naziv FROM korisnici LEFT JOIN lokacije AS trenutna ON kor_trenutna_lokacija = trenutna.lok_id LEFT JOIN lokacije AS uobicaj ON kor_lokacija = uobicaj.lok_id LEFT JOIN kategorije_prostorija ON uobicaj.lok_kategorija = kat_id WHERE kor_id =  @usrId";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.Int).Value = userIdRequest.UserId;
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        if (data.Read())
                        {
                            int idc = 0;
                            bool check = Int32.TryParse(data["kor_id"].ToString(), out idc);
                            if (check == true && idc != 0)
                            {
                                user.Id = int.Parse(data["kor_id"].ToString());
                                user.userName = data["kor_username"].ToString();
                                user.passWord = data["kor_lozinka"].ToString();
                                user.name = data["kor_ime"].ToString();
                                user.locationId = int.Parse(data["lok_id"].ToString());
                                user.locationName=data["kor_lokacija"].ToString();
                                user.sector = data["kat_naziv"].ToString();
                                user.currentLocarion = data["trenutna_lokacija"].ToString();
                                return Ok(user);
                            }
                                
                        }
                    }
                }

            }
            
            return NotFound();
            
        }



    }
}
