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
    public class CategoryController : ApiController
    {
        public IEnumerable<Category> GetAllCategory()
        {

            List<Category> categoris = new List<Category>();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT kat_id, kat_naziv FROM kategorije_prostorija";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    //cmd.Parameters.Add("@pa1",System.Data.SqlDbType.VarChar).Value="test";
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        while (data.Read())
                        {
                            Category category = new Category();
                            category.catId = int.Parse(data["kat_id"].ToString());
                            category.catName = data["kat_naziv"].ToString();
                            categoris.Add(category);
                        }
                    }
                }

            }

            return categoris;
        }
    }
}
