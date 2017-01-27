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
    public class LocationController : ApiController
    {
        public IEnumerable<Location> GetAllLocations()
        {
            
            List<Location> locations = new List<Location>();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT lok_id, lok_naziv, lok_opis, lok_ble_mac,kat_naziv FROM lokacije LEFT JOIN kategorije_prostorija ON lok_kategorija = kat_id ";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    //cmd.Parameters.Add("@pa1",System.Data.SqlDbType.VarChar).Value="test";
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        while (data.Read())
                        {
                            Location locationRead = new Location();
                            locationRead.Id = int.Parse(data["lok_id"].ToString());
                            locationRead.name = data["lok_naziv"].ToString();
                            locationRead.description = data["lok_opis"].ToString();
                            locationRead.macAddress = data["lok_ble_mac"].ToString();
                            locationRead.category = data["kat_naziv"].ToString();
                            locations.Add(locationRead);
                        }
                    }
                }

            }

            return locations;
        }
        [HttpPost]
        public IHttpActionResult GetLocation([FromBody] Mac loginRequest)
        {
            Location location = new Location();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT lok_id, lok_naziv, lok_opis, lok_ble_mac,kat_naziv FROM lokacije LEFT JOIN kategorije_prostorija ON lok_kategorija = kat_id WHERE lok_ble_mac=@mac_address; INSERT INTO povijest_kretanja SELECT @usrId, lok_id, GETDATE() FROM lokacije WHERE lok_ble_mac=@mac_address; UPDATE korisnici SET kor_trenutna_lokacija = (SELECT lok_id FROM lokacije WHERE lok_ble_mac=@mac_address) WHERE kor_id = @usrId;";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@mac_address", System.Data.SqlDbType.VarChar).Value = loginRequest.MacAddress;
                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.VarChar).Value = loginRequest.UsrId;

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        if (data.Read())
                        {
                            int id = 0;
                            bool check = Int32.TryParse(data["lok_id"].ToString(), out id);
                            if (check == true && id != 0)
                            {
                                location.Id = int.Parse(data["lok_id"].ToString());
                                location.name = data["lok_naziv"].ToString();
                                location.description = data["lok_opis"].ToString();
                                location.macAddress = data["lok_ble_mac"].ToString();
                                location.category = data["kat_naziv"].ToString();
                                return Ok(location);
                            }
                        }
                        return NotFound();
                    }
                }
            }
            
        }


    }
}
