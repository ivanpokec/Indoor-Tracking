using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace IndoorTracking.Models
{
    public class UserMovement
    {
        public string description { get; internal set; }
        public string name { get; internal set; }
        public string time { get; internal set; }
        public string user { get; internal set; }
    }
}