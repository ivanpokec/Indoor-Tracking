using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace IndoorTracking.Services
{
    public class Server
    {
        public static string ConnectionString
        {
            get
            {
                return System.Configuration.ConfigurationManager.ConnectionStrings["MyConnectionString"].ConnectionString;
            }
        }
    }
}