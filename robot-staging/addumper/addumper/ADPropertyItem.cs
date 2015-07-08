// -----------------------------------------------------------------------
// <copyright file="ADPropertyItem.cs" company="Microsoft">
// Copyright (c) Microsoft Corporation.  All rights reserved.
// </copyright>
// -----------------------------------------------------------------------

namespace addumper
{
    using System;
    using System.Collections.Generic;
    using System.Text;

    /// <summary>
    /// Represents a single AD property.
    /// </summary>
    public class ADPropertyItem
    {
        public string Name { get; set; }
        public string Type { get; set; }
        public string Value { get; set; }
    }
}
