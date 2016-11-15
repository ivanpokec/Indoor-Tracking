using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace IndoorTracking.Models
{
    public class Location
    {
        public int Id { get; set; }
        public string name { get; set; }
        public string macAddress { get; set; }
        public string description { get; set; }
    }
}