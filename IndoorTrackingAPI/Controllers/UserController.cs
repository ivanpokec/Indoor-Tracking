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
                string strCmd = @"SELECT kor_id, 
		                                kor_username, 
		                                kor_lozinka,
		                                kor_ime,
		                                kor_lokacija AS kor_lokacija_id, 
		                                uobicaj.lok_naziv AS kor_lokacija_naziv, 
		                                uobicajena.kat_naziv AS kor_lokacija_kategorija,
		                                kor_trenutna_lokacija AS trenutna_lokacija_id, 
		                                trenutna.lok_naziv AS trenutna_lokacija_naziv,
		                                trenutna_kat.kat_naziv AS trenutna_lokacija_kategorija,		
		                                kor_notification 
                                FROM korisnici 
                                LEFT JOIN lokacije AS trenutna ON kor_trenutna_lokacija = trenutna.lok_id 
                                LEFT JOIN lokacije AS uobicaj ON kor_lokacija = uobicaj.lok_id 
                                LEFT JOIN kategorije_prostorija AS uobicajena ON uobicaj.lok_kategorija = uobicajena.kat_id
                                LEFT JOIN kategorije_prostorija AS trenutna_kat ON trenutna.lok_kategorija = trenutna_kat.kat_id";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    //cmd.Parameters.Add("@pa1",System.Data.SqlDbType.VarChar).Value="test";
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            
                            //Filati strukturu users
                            User userRead = new User();
                            userRead.userId = int.Parse(data["kor_id"].ToString());
                            userRead.userName = data["kor_username"].ToString();
                            userRead.passWord = data["kor_lozinka"].ToString();
                            userRead.name = data["kor_ime"].ToString();
                            userRead.locationId = int.Parse(data["kor_lokacija_id"].ToString());
                            userRead.locationName = data["kor_lokacija_naziv"].ToString();
                            userRead.locationCategory = data["kor_lokacija_kategorija"].ToString();
                            userRead.currentLocationId = int.Parse(data["trenutna_lokacija_id"].ToString());
                            userRead.currentLocationName = data["trenutna_lokacija_naziv"].ToString();
                            userRead.currentLocationCategory = data["trenutna_lokacija_kategorija"].ToString();
                            userRead.notification = int.Parse(data["kor_notification"].ToString()); 

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
                string strCmd = @"SELECT kor_id, 
		                                kor_username, 
		                                kor_lozinka,
		                                kor_ime,
		                                kor_lokacija AS kor_lokacija_id, 
		                                uobicaj.lok_naziv AS kor_lokacija_naziv, 
		                                uobicajena.kat_naziv AS kor_lokacija_kategorija,
		                                kor_trenutna_lokacija AS trenutna_lokacija_id, 
		                                trenutna.lok_naziv AS trenutna_lokacija_naziv,
		                                trenutna_kat.kat_naziv AS trenutna_lokacija_kategorija,		
		                                kor_notification 
                                FROM korisnici 
                                LEFT JOIN lokacije AS trenutna ON kor_trenutna_lokacija = trenutna.lok_id 
                                LEFT JOIN lokacije AS uobicaj ON kor_lokacija = uobicaj.lok_id 
                                LEFT JOIN kategorije_prostorija AS uobicajena ON uobicaj.lok_kategorija = uobicajena.kat_id
                                LEFT JOIN kategorije_prostorija AS trenutna_kat ON trenutna.lok_kategorija = trenutna_kat.kat_id
                                    WHERE kor_id =  @usrId";
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
                                user.userId = int.Parse(data["kor_id"].ToString());
                                user.userName = data["kor_username"].ToString();
                                user.passWord = data["kor_lozinka"].ToString();
                                user.name = data["kor_ime"].ToString();
                                user.locationId = int.Parse(data["kor_lokacija_id"].ToString());
                                user.locationName = data["kor_lokacija_naziv"].ToString();
                                user.locationCategory = data["kor_lokacija_kategorija"].ToString();
                                user.currentLocationId = int.Parse(data["trenutna_lokacija_id"].ToString());
                                user.currentLocationName = data["trenutna_lokacija_naziv"].ToString();
                                user.currentLocationCategory = data["trenutna_lokacija_kategorija"].ToString();
                                user.notification = int.Parse(data["kor_notification"].ToString());
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
