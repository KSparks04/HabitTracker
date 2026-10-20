import { useState } from 'react';
import * as Icons from 'lucide-react';

interface IconSelectorProps {
    onSelect: (iconName: string) => void;
    selectedIcon?: string;
}

// A curated list of popular icon names from lucide-react
// You can add or remove any icon names you want here
const POPULAR_ICONS = [
    'Activity', 'Airplay', 'AlertCircle', 'AlertTriangle', 'AlignCenter',
    'AlignJustify', 'AlignLeft', 'AlignRight', 'Anchor', 'Aperture',
    'Archive', 'ArrowDown', 'ArrowLeft', 'ArrowRight', 'ArrowUp',
    'Award', 'BarChart', 'Battery', 'Bell', 'Bookmark',
    'Briefcase', 'Calendar', 'Camera', 'Check', 'ChevronDown',
    'ChevronLeft', 'ChevronRight', 'ChevronUp', 'Clock', 'Cloud',
    'Code', 'Compass', 'Copy', 'Cpu', 'CreditCard',
    'Database', 'Download', 'Edit', 'Eye', 'File',
    'Film', 'Filter', 'Folder', 'Globe', 'Grid',
    'Heart', 'Home', 'Image', 'Inbox', 'Info',
    'Key', 'Layers', 'Layout', 'LifeBuoy', 'Link',
    'Lock', 'LogIn', 'LogOut', 'Mail', 'Map',
    'Maximize', 'Menu', 'MessageSquare', 'Mic', 'Minimize',
    'Minus', 'Monitor', 'Moon', 'MoreHorizontal', 'MoreVertical',
    'Move', 'Music', 'Navigation', 'Octagon', 'Package',
    'Paperclip', 'Pause', 'Percent', 'Phone', 'PieChart',
    'Play', 'Plus', 'Power', 'Printer', 'Radio',
    'RefreshCw', 'Repeat', 'RotateCcw', 'RotateCw', 'Rss',
    'Save', 'Scissors', 'Search', 'Send', 'Server',
    'Settings', 'Share', 'Shield', 'ShoppingBag', 'ShoppingCart',
    'Shuffle', 'Sidebar', 'SkipBack', 'SkipForward', 'Slash',
    'Sliders', 'Smartphone', 'Speaker', 'Square', 'Star',
    'Sun', 'Sunrise', 'Sunset', 'Tablet', 'Tag',
    'Target', 'Terminal', 'ThumbsDown', 'ThumbsUp', 'ToggleLeft',
    'ToggleRight', 'Tool', 'Trash', 'TrendingDown', 'TrendingUp',
    'Triangle', 'Truck', 'Tv', 'Type', 'Umbrella',
    'Unlock', 'Upload', 'User', 'Users', 'Video',
    'Volume', 'Watch', 'Wifi', 'Wind', 'Wrench',
    'X', 'Zap', 'ZoomIn', 'ZoomOut'
];

export default function IconSelector({ onSelect, selectedIcon }: IconSelectorProps) {
    const [searchQuery, setSearchQuery] = useState('');

    // Filter icons based on the search input
    const filteredIcons = POPULAR_ICONS.filter((name) =>
        name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <div className="border rounded-lg p-4 bg-white shadow-sm w-full max-w-md">
            {/* Search Input */}
            <input
                type="text"
                placeholder="Search icons..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full px-3 py-2 border rounded-md mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />

            {/* Icon Grid */}
            <div className="grid grid-cols-5 gap-2 max-h-60 overflow-y-auto p-1">
                {filteredIcons.map((iconName) => {
                    // Dynamically grab the component from lucide-react
                    const IconComponent = (Icons as any)[iconName];
                    const isSelected = selectedIcon === iconName;

                    if (!IconComponent) return null;

                    return (
                        <button
                            key={iconName}
                            type="button"
                            onClick={() => onSelect(iconName)}
                            title={iconName}
                            className={`flex flex-col items-center justify-center p-3 rounded-lg border transition-all ${
                                isSelected
                                    ? 'border-blue-500 bg-blue-50 text-blue-600'
                                    : 'border-gray-200 hover:bg-gray-50 text-gray-700'
                            }`}
                        >
                            <IconComponent className="w-5 h-5 mb-1" />
                            <span className="text-[10px] truncate w-full text-center">
                {iconName}
              </span>
                        </button>
                    );
                })}

                {filteredIcons.length === 0 && (
                    <p className="col-span-5 text-center text-sm text-gray-500 py-4">
                        No icons found
                    </p>
                )}
            </div>
        </div>
    );
}