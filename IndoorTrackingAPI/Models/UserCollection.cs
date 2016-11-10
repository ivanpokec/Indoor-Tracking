using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Web;

namespace IndoorTracking.Models
{
    public class UserCollection : Collection<User>
    {
        public bool CheckIfExist(int id)
        {
            return true;
        }
    }
}