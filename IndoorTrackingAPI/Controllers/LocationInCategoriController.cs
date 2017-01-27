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
    public class LocationInCategoriController : ApiController
    {
        [HttpPost]
        public IHttpActionResult GetHistory([FromBody] CategoryId categori)
        {
            List<Location> locationsInCategori = new List<Location>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT lok_id, lok_naziv, lok_ble_mac, lok_opis, kat_naziv FROM lokacije LEFT JOIN kategorije_prostorija ON lok_kategorija = kat_id WHERE lok_kategorija = @location";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@location", System.Data.SqlDbType.Int).Value = int.Parse(categori.catId.ToString());

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
                            
                            locationsInCategori.Add(locationRead);
                        }
                        if (locationsInCategori.Count() > 0)
                        {
                            return Ok(locationsInCategori);
                        }
                        else
                        {
                            return NotFound();
                        }
                    }
                }
            }
        }
    }
}
